package com.biblioteca.entity;

import jakarta.persistence.*;

// Todo professor é também um funcionário (tabela professor aponta para funcionario)
@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professor")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_funcionario", nullable = false, unique = true)
    private Funcionario funcionario;

    @Column(nullable = false, length = 80)
    private String disciplina;

    public Professor() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }
}
