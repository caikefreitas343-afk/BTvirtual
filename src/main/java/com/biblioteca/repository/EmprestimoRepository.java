package com.biblioteca.repository;


import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    List<Emprestimo> findByUsuarioId(UUID usuarioId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);
}