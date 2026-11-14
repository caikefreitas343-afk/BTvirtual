package com.biblioteca.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

// Um Livro é o TÍTULO (a obra). As cópias físicas são os Exemplares.
@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_editora", nullable = false)
    private Editora editora;

    @Column(nullable = false, length = 250)
    private String titulo;

    @Column(length = 30)
    private String edicao;

    @Column(name = "ano_publicacao")
    private Short anoPublicacao;

    // Tabela de ligação livro_autor
    @ManyToMany
    @JoinTable(name = "livro_autor",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_autor"))
    private Set<Autor> autores = new HashSet<>();

    // Tabela de ligação livro_categoria
    @ManyToMany
    @JoinTable(name = "livro_categoria",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    private Set<Categoria> categorias = new HashSet<>();

    public Livro() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Editora getEditora() { return editora; }
    public void setEditora(Editora editora) { this.editora = editora; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) { this.edicao = edicao; }

    public Short getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Short anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Set<Autor> getAutores() { return autores; }
    public void setAutores(Set<Autor> autores) { this.autores = autores; }

    public Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(Set<Categoria> categorias) { this.categorias = categorias; }
}
