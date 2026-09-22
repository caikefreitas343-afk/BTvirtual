package com.biblioteca.repository;

import com.biblioteca.entity.Aluno;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    Optional<Aluno> findByMatricula(String matricula);
    Optional<Aluno> findByEmail(String email);
}