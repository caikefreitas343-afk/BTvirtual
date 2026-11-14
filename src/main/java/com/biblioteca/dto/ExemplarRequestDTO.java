package com.biblioteca.dto;

import java.time.LocalDate;

// Usado para criar e atualizar um exemplar. Se "disponivel" não vier, fica como está (ou true, no cadastro).
public record ExemplarRequestDTO(
        Integer idLivro,
        String numeroTombo,
        String estadoConservacao,
        LocalDate dataAquisicao,
        Boolean disponivel
) {
}
