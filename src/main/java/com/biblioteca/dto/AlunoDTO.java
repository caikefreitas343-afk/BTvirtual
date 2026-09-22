package com.biblioteca.dto;

import com.biblioteca.entity.Aluno;


public record AlunoDTO(
        String nome,
        String matricula,
        String email
) {
}
