package com.biblioteca.dto;

import java.util.UUID;

public record AtualizarAlunoDTO(
        UUID id,
        String nome,
        String matricula,
        String email,
        String turma,
        String serie,
        String novaSenha
) {
}
