package com.biblioteca.repository;

import com.biblioteca.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    List<Reserva> findByUsuarioId(UUID usuarioId);

    List<Reserva> findByLivroIdAndAtiva(UUID livroId, boolean ativa);

    Optional<Reserva> findByUsuarioIdAndLivroIdAndAtiva(UUID usuarioId, UUID livroId, boolean ativa);
}
