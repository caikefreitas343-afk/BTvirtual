package com.biblioteca.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exemplar")
public class Exemplar {
    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusExemplar status = StatusExemplar.DISPONIVEL;

    @Column(length = 100)
    private String localizacao;

    // Construtor vazio (Obrigatorio para o JPA)
    public Exemplar() {}

    public Exemplar(String codigo, Livro livro, String localizacao) {
        this.codigo = codigo;
        this.livro = livro;
        this.localizacao = localizacao;
        this.status = StatusExemplar.DISPONIVEL; // Exemplar novo é sempre criado como DISPONIVEL
    }

    // Getters e Setters
    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}

    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}

    public Livro getLivro() {return livro;}
    public void setLivro(Livro livro) {this.livro = livro;}

    public StatusExemplar getStatus() {return status;}
    public void setStatus(StatusExemplar status) {this.status = status;}

    public String getLocalizacao() {return localizacao;}
    public void setLocalizacao(String localizacao) {this.localizacao = localizacao;}
}
