package com.biblioteca.repository;

import com.biblioteca.entity.Exemplar;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExemplarRepository extends JpaRepository<Exemplar, Integer> {

    Optional<Exemplar> findByNumeroTombo(String numeroTombo);

    boolean existsByNumeroTombo(String numeroTombo);

    // Todas as cópias de um livro
    List<Exemplar> findByLivroId(Integer livroId);

    long countByLivroId(Integer livroId);

    long countByLivroIdAndDisponivelTrue(Integer livroId);

    // Trava o exemplar enquanto o empréstimo é feito (dois alunos não pegam a mesma cópia)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Exemplar e where e.id = :id")
    Optional<Exemplar> findByIdForUpdate(@Param("id") Integer id);
}
