package com.biblioteca.dto;

import com.biblioteca.entity.Reserva;

public record ReservaDTO(
	String nomeUsuario,
	String tituloLivro,
	String dataReserva,
	String status
) {
	public ReservaDTO(Reserva reserva) {
		this(
			reserva.getUsuario() != null ? reserva.getUsuario().getNome() : null,
			reserva.getLivro() != null ? reserva.getLivro().getTitulo() : null,
			reserva.getDataReserva() != null ? reserva.getDataReserva().toString() : null,
            reserva.isAtiva() ? "ATIVA" : "INATIVA"
		);
	}
}

