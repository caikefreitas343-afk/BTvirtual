package com.biblioteca.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_aluno")
public class Aluno extends Usuario {

    private String turma;
    private String serie;
    private boolean bloqueado = false; //Controla impedimento por multas e atrasos

    //Contrutor Vazio (Exigido pelo JPA)
    public Aluno() {
        super();
        this.tipoUsuario = TipoUsuario.ALUNO;
    }

    // Construtor completo
    public Aluno(UUID id, String nome, String matricula, String email, String senha, String turma, String serie) {
        super(id, nome, matricula, email, senha, TipoUsuario.ALUNO);
        this.turma = turma;
        this.serie = serie;
        this.bloqueado = false; //Aluno novo começa liberado
    }

    // Getters e Setters
    public String getTurma() { return turma;}
    public void setTurma(String turma) { this.turma = turma; }

    public String getSerie() { return serie;}
    public void setSerie(String serie) { this.serie = serie;}

    public boolean isBloqueado() { return bloqueado; }
    
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado;}
}


