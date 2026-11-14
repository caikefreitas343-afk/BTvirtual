package com.biblioteca.dto;

import java.math.BigDecimal;

// Multa aplicada manualmente pelo bibliotecário (ex.: livro perdido). Máximo 99,99.
public record MultaRequestDTO(
        Integer idAluno,
        BigDecimal valor
) {
}
