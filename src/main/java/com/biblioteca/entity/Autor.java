package com.biblioteca.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Integer id;

    @Column(name = "nome_artistico", length = 120)
    private String nomeArtistico;

    @Column(name = "prenome_autor", nullable = false, length = 80)
    private String prenomeAutor;

    @Column(name = "sobrenome_autor", nullable = false, length = 80)
    private String sobrenomeAutor;

    public Autor() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeArtistico() { return nomeArtistico; }
    public void setNomeArtistico(String nomeArtistico) { this.nomeArtistico = nomeArtistico; }

    public String getPrenomeAutor() { return prenomeAutor; }
    public void setPrenomeAutor(String prenomeAutor) { this.prenomeAutor = prenomeAutor; }

    public String getSobrenomeAutor() { return sobrenomeAutor; }
    public void setSobrenomeAutor(String sobrenomeAutor) { this.sobrenomeAutor = sobrenomeAutor; }

    // Nome para exibir: usa o nome artístico, se existir
    public String getNomeExibicao() {
        if (nomeArtistico != null && !nomeArtistico.isBlank()) return nomeArtistico;
        return prenomeAutor + " " + sobrenomeAutor;
    }
}
