package com.biblioteca.dto;

import com.biblioteca.entity.Emprestimo;

public record EmprestimoDTO(
        String nomeUsuario,
        String tituloLivro,
        String dataEmprestimo,
        String dataDevolucao
) {
    public EmprestimoDTO(Emprestimo emprestimo) {
        this(
        emprestimo.getUsuario() != null ? emprestimo.getUsuario().getNome() : null,
        emprestimo.getLivro() != null ? emprestimo.getLivro().getTitulo() : null,
        emprestimo.getDataEmprestimo() != null ? emprestimo.getDataEmprestimo().toString() : null,
        emprestimo.getDataDevolucaoPrevista() != null ? emprestimo.getDataDevolucaoPrevista().toString() : null
        );
    }
}