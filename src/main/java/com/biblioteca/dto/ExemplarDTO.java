package com.biblioteca.dto;

import java.util.UUID;

import com.biblioteca.entity.Exemplar;
import com.biblioteca.entity.StatusExemplar;

// Resposta da API
public record ExemplarDTO(
    UUID id,
    String codigo,
    String tituloLivro,
    StatusExemplar status,
    String localizacao
) {
    public ExemplarDTO(Exemplar exemplar) {
        this(
            exemplar.getId(),
            exemplar.getCodigo(),
            exemplar.getLivro().getTitulo(),
            exemplar.getStatus(),
            exemplar.getLocalizacao()
        );
    }
}
