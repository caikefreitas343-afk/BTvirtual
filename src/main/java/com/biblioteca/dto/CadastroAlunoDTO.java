package com.biblioteca.dto;

public class CadastroAlunoDTO {
    private String nome;
    private String matricula;
    private String email;
    private String senha;
    private String turma;
    private String serie;

    public CadastroAlunoDTO() {
    }

    public CadastroAlunoDTO(String nome, String matricula, String email, String senha, String turma, String serie) {
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.senha = senha;
        this.turma = turma;
        this.serie = serie;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
