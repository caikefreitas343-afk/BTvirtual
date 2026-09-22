package com.biblioteca.repository;

import com.biblioteca.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    List<Reserva> findByUsuarioId(UUID usuarioId);

    List<Reserva> findByLivroIdAndAtivaTrue(UUID livroId, boolean ativa);
}
