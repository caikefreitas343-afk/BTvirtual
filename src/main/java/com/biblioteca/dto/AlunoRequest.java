package com.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlunoRequest(
        @NotBlank String prenome,
        @NotBlank String sobrenome,
        @NotBlank String matricula,
        @NotNull Integer idTurma,
        String email,
        String senha
) {}