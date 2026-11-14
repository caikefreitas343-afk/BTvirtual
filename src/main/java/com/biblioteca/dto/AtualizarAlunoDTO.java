package com.biblioteca.dto;

// novaSenha é opcional: se vier vazia ou nula, a senha atual é mantida
public record AtualizarAlunoDTO(
        String prenome,
        String sobrenome,
        String matricula,
        String email,
        Integer idTurma,
        String novaSenha
) {
}
