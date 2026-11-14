package com.biblioteca.dto;

import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.StatusEmprestimo;

import java.time.LocalDate;

public record EmprestimoDTO(
        Integer id,
        Integer idExemplar,
        String numeroTombo,
        String tituloLivro,
        Integer idAluno,
        Integer idProfessor,
        String nomeSolicitante,
        Integer idFuncionario,
        String nomeFuncionario,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        StatusEmprestimo status,
        boolean atrasado
) {
    public static EmprestimoDTO from(Emprestimo e) {
        boolean ehAluno = e.getAluno() != null;
        String nomeSolicitante = ehAluno
                ? e.getAluno().getNomeCompleto()
                : (e.getProfessor() != null ? e.getProfessor().getFuncionario().getNome() : null);

        boolean emAberto = e.getStatus() == StatusEmprestimo.ATIVO || e.getStatus() == StatusEmprestimo.ATRASADO;
        boolean atrasado = emAberto && LocalDate.now().isAfter(e.getDataPrevistaDevolucao());

        return new EmprestimoDTO(
                e.getId(),
                e.getExemplar().getId(),
                e.getExemplar().getNumeroTombo(),
                e.getExemplar().getLivro().getTitulo(),
                ehAluno ? e.getAluno().getId() : null,
                e.getProfessor() != null ? e.getProfessor().getId() : null,
                nomeSolicitante,
                e.getFuncionario().getId(),
                e.getFuncionario().getNome(),
                e.getDataEmprestimo(),
                e.getDataPrevistaDevolucao(),
                e.getStatus(),
                atrasado
        );
    }
}
