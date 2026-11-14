package com.biblioteca.dto;

import java.time.LocalDate;

// Usado para criar e para atualizar. Na atualização a senha é opcional.
public record FuncionarioRequestDTO(
        String nome,
        String email,
        LocalDate dataAdmissao,
        String cargo,
        String matriculaFuncional,
        String senha
) {
}
