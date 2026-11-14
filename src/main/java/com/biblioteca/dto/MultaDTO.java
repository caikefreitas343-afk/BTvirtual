package com.biblioteca.dto;

import com.biblioteca.entity.Multa;
import com.biblioteca.entity.StatusMulta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MultaDTO(
        Integer id,
        BigDecimal valor,
        StatusMulta status,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer idAluno,
        String nomeAluno
) {
    public static MultaDTO from(Multa m) {
        return new MultaDTO(
                m.getId(),
                m.getValor(),
                m.getStatus(),
                m.getDataInicio(),
                m.getDataFim(),
                m.getAluno() != null ? m.getAluno().getId() : null,
                m.getAluno() != null ? m.getAluno().getNomeCompleto() : null
        );
    }
}
