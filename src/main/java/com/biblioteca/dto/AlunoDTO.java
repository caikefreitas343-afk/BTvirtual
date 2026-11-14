package com.biblioteca.dto;

import com.biblioteca.entity.Aluno;

// Resposta da API: NUNCA inclui a senha
public record AlunoDTO(
        Integer id,
        String prenome,
        String sobrenome,
        String matricula,
        String email,
        Integer idTurma,
        String turma,
        String serie,
        boolean bloqueado
) {
    public static AlunoDTO from(Aluno a, boolean bloqueado) {
        return new AlunoDTO(
                a.getId(),
                a.getPrenome(),
                a.getSobrenome(),
                a.getMatricula(),
                a.getEmail(),
                a.getTurma().getId(),
                a.getTurma().getCodigoTurma(),
                a.getTurma().getSerie(),
                bloqueado
        );
    }
}
