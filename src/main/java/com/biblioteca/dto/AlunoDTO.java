package com.biblioteca.dto;

import java.util.UUID;

import com.biblioteca.entity.Aluno;

public record AlunoDTO(
        UUID id,
        String nome,
        String matricula,
        String email,
        String turma,
        String serie,
        boolean bloqueado
) {
    public AlunoDTO(Aluno aluno) {
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getMatricula(),
                aluno.getEmail(),
                aluno.getTurma(),
                aluno.getSerie(),
                aluno.isBloqueado()
        );
    }
}