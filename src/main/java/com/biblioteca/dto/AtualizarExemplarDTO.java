package com.biblioteca.dto; 

import com.biblioteca.entity.StatusExemplar;

public record AtualizarExemplarDTO(
    String codigo,
    String localizacao,
    StatusExemplar status
) {}