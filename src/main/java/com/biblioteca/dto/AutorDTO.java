package com.biblioteca.dto;

import com.biblioteca.entity.Autor;

public record AutorDTO(Integer id, String nomeArtistico, String prenomeAutor, String sobrenomeAutor) {

    public static AutorDTO from(Autor a) {
        return new AutorDTO(a.getId(), a.getNomeArtistico(), a.getPrenomeAutor(), a.getSobrenomeAutor());
    }
}
