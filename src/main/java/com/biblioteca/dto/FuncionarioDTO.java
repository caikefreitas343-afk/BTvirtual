package com.biblioteca.dto;

import com.biblioteca.entity.Funcionario;

import java.time.LocalDate;

// Resposta da API: NUNCA inclui a senha
public record FuncionarioDTO(
        Integer id,
        String nome,
        String email,
        LocalDate dataAdmissao,
        String cargo,
        String matriculaFuncional
) {
    public static FuncionarioDTO from(Funcionario f) {
        return new FuncionarioDTO(
                f.getId(),
                f.getNome(),
                f.getEmail(),
                f.getDataAdmissao(),
                f.getCargo(),
                f.getMatriculaFuncional()
        );
    }
}
