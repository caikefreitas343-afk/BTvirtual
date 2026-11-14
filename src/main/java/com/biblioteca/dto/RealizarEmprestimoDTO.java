package com.biblioteca.dto;

// Informe idAluno OU idProfessor (apenas um dos dois)
public record RealizarEmprestimoDTO(
        Integer idExemplar,
        Integer idFuncionario,
        Integer idAluno,
        Integer idProfessor
) {
}
