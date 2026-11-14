package com.biblioteca.dto;

import com.biblioteca.entity.Categoria;

public record CategoriaDTO(Integer id, String nomeCategoria) {

    public static CategoriaDTO from(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNomeCategoria());
    }
}
