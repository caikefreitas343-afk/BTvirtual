package com.biblioteca.dto;

import com.biblioteca.entity.Exemplar;

import java.time.LocalDate;

public record ExemplarDTO(
        Integer id,
        String numeroTombo,
        Integer idLivro,
        String tituloLivro,
        String estadoConservacao,
        LocalDate dataAquisicao,
        boolean disponivel
) {
    public static ExemplarDTO from(Exemplar e) {
        return new ExemplarDTO(
                e.getId(),
                e.getNumeroTombo(),
                e.getLivro().getId(),
                e.getLivro().getTitulo(),
                e.getEstadoConservacao(),
                e.getDataAquisicao(),
                e.isDisponivel()
        );
    }
}
