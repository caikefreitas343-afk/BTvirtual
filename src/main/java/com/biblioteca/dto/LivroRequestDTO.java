package com.biblioteca.dto;

import java.util.List;

public record LivroRequestDTO(
        String titulo,
        String edicao,
        Short anoPublicacao,
        Integer idEditora,
        List<Integer> idsAutores,
        List<Integer> idsCategorias
) {
}
