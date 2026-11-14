package com.biblioteca.dto;

import com.biblioteca.entity.Turma;

public record TurmaDTO(Integer id, String codigoTurma, String serie, String turno, Short anoLetivo) {

    public static TurmaDTO from(Turma t) {
        return new TurmaDTO(t.getId(), t.getCodigoTurma(), t.getSerie(), t.getTurno(), t.getAnoLetivo());
    }
}
