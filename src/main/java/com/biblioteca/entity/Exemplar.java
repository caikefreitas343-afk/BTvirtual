package com.biblioteca.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

// Um Exemplar é UMA CÓPIA FÍSICA de um Livro (o título).
@Entity
@Table(name = "exemplar")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exemplar")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    // Código da etiqueta colada no livro físico
    @Column(name = "numero_tombo", nullable = false, unique = true, length = 30)
    private String numeroTombo;

    @Column(name = "estado_conservacao", nullable = false, length = 20)
    private String estadoConservacao;

    @Column(name = "data_aquisicao", nullable = false)
    private LocalDate dataAquisicao;

    @Column(nullable = false)
    private boolean disponivel = true;

    public Exemplar() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public String getNumeroTombo() { return numeroTombo; }
    public void setNumeroTombo(String numeroTombo) { this.numeroTombo = numeroTombo; }

    public String getEstadoConservacao() { return estadoConservacao; }
    public void setEstadoConservacao(String estadoConservacao) { this.estadoConservacao = estadoConservacao; }

    public LocalDate getDataAquisicao() { return dataAquisicao; }
    public void setDataAquisicao(LocalDate dataAquisicao) { this.dataAquisicao = dataAquisicao; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}
