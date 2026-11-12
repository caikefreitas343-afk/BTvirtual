package com.biblioteca.dto;

import java.util.UUID;

public record CadastroExemplarDTO(
    UUID id,
    String codigo,
    String localizacao
) {}