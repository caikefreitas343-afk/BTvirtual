package com.biblioteca.repository;


import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.StatusEmprestimo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    List<Emprestimo> findByUsuarioId(UUID usuarioId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Emprestimo e where e.id = :id")
    Optional<Emprestimo> findByIdForUpdate(@Param("id") UUID id);
}