package com.biblioteca.dto;

import com.biblioteca.entity.Aluno;

public record AlunoDTO(
        String nome,
        String matricula,
        String email
) {
    public AlunoDTO(String nome, String matricula, String email) {
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
    }

    public AlunoDTO(Aluno aluno) {
        this(
                aluno.getNome(),
                aluno.getMatricula(),
                aluno.getEmail()
        );
    }
}