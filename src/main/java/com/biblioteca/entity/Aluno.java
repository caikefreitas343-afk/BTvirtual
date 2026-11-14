package com.biblioteca.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_turma", nullable = false)
    private Turma turma;

    @Column(nullable = false, unique = true, length = 30)
    private String matricula;

    @Column(nullable = false, length = 80)
    private String prenome;

    @Column(nullable = false, length = 80)
    private String sobrenome;

    // Colunas acrescentadas ao banco para o login (veja alteracoes_banco.sql)
    @Column(length = 160)
    private String email;

    @Column(length = 100)
    private String senha;

    public Aluno() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getPrenome() { return prenome; }
    public void setPrenome(String prenome) { this.prenome = prenome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getNomeCompleto() { return prenome + " " + sobrenome; }
}
