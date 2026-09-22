package com.biblioteca.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(nullable = true, unique = true, length = 20)
    private String isbn;

    @Column (nullable = false)
    private int anoPublicacao;

    @Column (nullable = false, length = 50)
    private String genero;

    @Column (nullable = false)
    private Integer quantidadeEstoque;

    @Column (nullable = false)
    private Integer quantidadeDisponivel;

    @Column (nullable = false, length = 50)
    private String codigoBarras;

    @Enumerated (EnumType.STRING)
    private StatusLivro status = StatusLivro.DISPONIVEL;

    public Livro() {}

    public Livro(String titulo, String autor, String isbn, int anoPublicacao, String genero, Integer quantidadeEstoque, Integer quantidadeDisponivel, String codigoBarras, UUID id) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.codigoBarras = codigoBarras;
        this.status = StatusLivro.DISPONIVEL;

    }

        public UUID getId() {return id;}
        public void setId(UUID id) {this.id = id;}

        public String getTitulo() {return titulo;}
        public void setTitulo(String titulo) {this.titulo = titulo;}

        public String getAutor() {return autor;}
        public void setAutor(String autor) {this.autor = autor;}

        public String getIsbn() {return isbn;}
        public void setIsbn(String isbn) {this.isbn = isbn;}

        public int getAnoPublicacao() {return anoPublicacao;}
        public void setAnoPublicacao(int anoPublicacao) {this.anoPublicacao = anoPublicacao;}

        public String getGenero() {return genero;}
        public void setGenero(String genero) {this.genero = genero;}

        public Integer getQuantidadeEstoque() {return quantidadeEstoque;}
        public void setQuantidadeEstoque(Integer quantidadeEstoque) {this.quantidadeEstoque = quantidadeEstoque;}

        public Integer getQuantidadeDisponivel() {return quantidadeDisponivel;}
        public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {this.quantidadeDisponivel = quantidadeDisponivel;}

        public String getCodigoBarras() {return codigoBarras;}
        public void setCodigoBarras(String codigoBarras) {this.codigoBarras = codigoBarras;}

        public StatusLivro getStatus() {return status;}
        public void setStatus(StatusLivro status) {this.status = status;}
    


}