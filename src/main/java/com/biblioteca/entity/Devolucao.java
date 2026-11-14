package com.biblioteca.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "devolucao")
public class Devolucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucao")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_emprestimo", nullable = false, unique = true)
    private Emprestimo emprestimo;

    @Column(name = "data_devolucao_efetiva", nullable = false)
    private LocalDate dataDevolucaoEfetiva;

    @Column(name = "estado_exemplar_devolvido", nullable = false, length = 20)
    private String estadoExemplarDevolvido;

    @Column(name = "multa_gerada", nullable = false)
    private boolean multaGerada = false;

    @Column(length = 500)
    private String observacoes;

    public Devolucao() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Emprestimo getEmprestimo() { return emprestimo; }
    public void setEmprestimo(Emprestimo emprestimo) { this.emprestimo = emprestimo; }

    public LocalDate getDataDevolucaoEfetiva() { return dataDevolucaoEfetiva; }
    public void setDataDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva) { this.dataDevolucaoEfetiva = dataDevolucaoEfetiva; }

    public String getEstadoExemplarDevolvido() { return estadoExemplarDevolvido; }
    public void setEstadoExemplarDevolvido(String estadoExemplarDevolvido) { this.estadoExemplarDevolvido = estadoExemplarDevolvido; }

    public boolean isMultaGerada() { return multaGerada; }
    public void setMultaGerada(boolean multaGerada) { this.multaGerada = multaGerada; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
