package com.biblioteca.dto;

import com.biblioteca.entity.Livro;

public record LivroDTO(
        String titulo,
        String autor,
        String isbn,
        int anoPublicacao,
        String genero,
        Integer quantidadeEstoque,
        Integer quantidadeDisponivel,
        String codigoBarras
) {
    public LivroDTO(String titulo, String autor, String isbn, int anoPublicacao, String genero, Integer quantidadeEstoque, Integer quantidadeDisponivel, String codigoBarras) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.codigoBarras = codigoBarras;
    }

    public LivroDTO(Livro livro) {
        this(
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getGenero(),
                livro.getQuantidadeEstoque(),
                livro.getQuantidadeDisponivel(),
                livro.getCodigoBarras()
        );
        
    }
}