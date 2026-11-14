package com.biblioteca.dto;

import com.biblioteca.entity.Livro;

import java.util.Comparator;
import java.util.List;

public record LivroDTO(
        Integer id,
        String titulo,
        String edicao,
        Short anoPublicacao,
        Integer idEditora,
        String editora,
        List<AutorDTO> autores,
        List<CategoriaDTO> categorias,
        long exemplaresTotal,
        long exemplaresDisponiveis
) {
    public static LivroDTO from(Livro l, long total, long disponiveis) {
        return new LivroDTO(
                l.getId(),
                l.getTitulo(),
                l.getEdicao(),
                l.getAnoPublicacao(),
                l.getEditora().getId(),
                l.getEditora().getNome(),
                l.getAutores().stream()
                        .map(AutorDTO::from)
                        .sorted(Comparator.comparing(AutorDTO::prenomeAutor))
                        .toList(),
                l.getCategorias().stream()
                        .map(CategoriaDTO::from)
                        .sorted(Comparator.comparing(CategoriaDTO::nomeCategoria))
                        .toList(),
                total,
                disponiveis
        );
    }
}
