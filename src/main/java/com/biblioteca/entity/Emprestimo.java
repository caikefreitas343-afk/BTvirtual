package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tb_emprestimos")
public class Emprestimo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    // Esse LAZY evita consultas gigantes desnecessárias no banco
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(nullable = false)
    private LocalDateTime dataEmprestimo;

    @Column(nullable = false)
    private LocalDateTime dataDevolucaoPrevista;

    // Pode ser nulo, porque quando o empréstimo é criado, ainda não foi devolvido
    private LocalDateTime dataDevolucaoEfetiva;

    @Column(nullable = false)
    private Integer quantidadeRenovacoes = 0;

    // Utulizamos BigDecimal (Boa prática para dinheiro) Por exemplo, caso o Aluno perca um livro.
    @Enumerated (EnumType.STRING)
    @Column(nullable = false, precision = 10, scale = 2)
    private StatusEmprestimo status;

    private Double valorMulta = 0.0;

    // Construtor vazio que é: (Obrigatorio para o JPA)
    public Emprestimo() {}

    // O Construtor completo
    public Emprestimo(UUID id, Usuario usuario, Livro livro, LocalDateTime dataEmprestimo, LocalDateTime dataDevolucaoPrevista, LocalDateTime dataDevolucaoEfetiva, Integer quantidadeRenovacoes, StatusEmprestimo status, Double valorMulta) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
        this.quantidadeRenovacoes = quantidadeRenovacoes;
        this.status = status;
        this.valorMulta = valorMulta;

    }

    // Getters e Setters Para todos os atributos da classe Emprestimo
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDateTime dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDateTime getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDateTime dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public LocalDateTime getDataDevolucaoEfetiva() { return dataDevolucaoEfetiva; }
    public void setDataDevolucaoEfetiva(LocalDateTime dataDevolucaoEfetiva) { this.dataDevolucaoEfetiva = dataDevolucaoEfetiva; }

    public Integer getQuantidadeRenovacoes() { return quantidadeRenovacoes; }
    public void setQuantidadeRenovacoes(Integer quantidadeRenovacoes) { this.quantidadeRenovacoes = quantidadeRenovacoes; }

    public StatusEmprestimo getStatus() { return status; }
    public void setStatus(StatusEmprestimo status) { this.status = status; }

    public Double getValorMulta() { return valorMulta; }
    public void setValorMulta(Double valorMulta) { this.valorMulta = valorMulta; }
}
