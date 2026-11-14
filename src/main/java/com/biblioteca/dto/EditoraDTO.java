package com.biblioteca.dto;

import com.biblioteca.entity.Editora;

public record EditoraDTO(Integer id, String nome, String cnpj) {

    public static EditoraDTO from(Editora e) {
        return new EditoraDTO(e.getId(), e.getNome(), e.getCnpj());
    }
}
