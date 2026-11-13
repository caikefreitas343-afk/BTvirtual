package com.biblioteca.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.entity.Exemplar;
import com.biblioteca.entity.StatusExemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, UUID> {
    
    Optional<Exemplar> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    // Todas as copias de um livro
    List<Exemplar> findByLivroId(UUID livroId);

    // Quantas cópias de um livro existem no total
    Long countByLivroId(UUID livroId);

    // Quantas cópias de um livro estão num certo status (Ex: DISPOINÍVEL)
    long countByLivroIdAndStatus(UUID livroId, StatusExemplar statusExemplar);
}
