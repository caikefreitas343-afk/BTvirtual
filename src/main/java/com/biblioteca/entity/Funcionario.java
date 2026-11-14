package com.biblioteca.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

// Funcionário da escola. Aqui ele faz o papel de bibliotecário.
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(nullable = false, length = 60)
    private String cargo;

    @Column(name = "matricula_funcional", nullable = false, unique = true, length = 30)
    private String matriculaFuncional;

    // Coluna acrescentada ao banco para o login (veja alteracoes_banco.sql)
    @Column(length = 100)
    private String senha;

    public Funcionario() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getMatriculaFuncional() { return matriculaFuncional; }
    public void setMatriculaFuncional(String matriculaFuncional) { this.matriculaFuncional = matriculaFuncional; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
