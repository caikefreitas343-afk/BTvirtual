package com.biblioteca.dto;

public record CadastroAlunoDTO(
        String prenome,
        String sobrenome,
        String matricula,
        String email,
        String senha,
        Integer idTurma
) {
}
