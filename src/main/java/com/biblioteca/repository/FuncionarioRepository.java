package com.biblioteca.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, UUID> {

    Optional<Funcionario> findByMatricula(String matricula);
    Optional<Funcionario> findByEmail(String email);
}
