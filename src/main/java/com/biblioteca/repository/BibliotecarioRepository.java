package com.biblioteca.repository;

import java.util.Optional;
import java.util.UUID;

import com.biblioteca.entity.Bibliotecario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, UUID> {

    Optional<Bibliotecario> findByMatricula(String matricula);
    Optional<Bibliotecario> findByEmail(String email);
}
