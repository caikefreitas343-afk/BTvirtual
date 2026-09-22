package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Aluno ou Bibliotecário/Professor solicitante

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro; // Livro físico reservado

    @Column(nullable = false)
    private LocalDateTime dataReserva; // Define a data em que o pedido foi feito

    private LocalDateTime dataValidade; // Define o prazo máximo para retirar o livro após ele ficar disponível

    private boolean ativa = true; // Isso define se a reserva ainda está válida ou foi concluída/cancelada

    // Construtor Vazio
    public Reserva() {}

    // Construtor Completo
    public Reserva(UUID id, Usuario usuario, Livro livro, LocalDateTime dataReserva, LocalDateTime dataValidade) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataReserva = dataReserva;
        this.dataValidade = dataValidade;
        this.ativa = true;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public LocalDateTime getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDateTime dataReserva) { this.dataReserva = dataReserva; }

    public LocalDateTime getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDateTime dataValidade) { this.dataValidade = dataValidade; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}