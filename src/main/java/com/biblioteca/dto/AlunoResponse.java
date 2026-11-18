package com.biblioteca.dto;

import com.biblioteca.entity.Aluno;

public record AlunoResponse(
        Integer id,
        String prenome,
        String sobrenome,
        String matricula,
        String email,
        Integer idTurma
) {
    public static AlunoResponse de(Aluno a) {
        return new AlunoResponse(
                a.getId(), a.getPrenome(), a.getSobrenome(),
                a.getMatricula(), a.getEmail(), a.getTurma().getId());
    }
}