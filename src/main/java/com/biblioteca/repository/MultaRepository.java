package com.biblioteca.repository;

import com.biblioteca.entity.Multa;
import com.biblioteca.entity.StatusMulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultaRepository extends JpaRepository<Multa, Integer> {

    List<Multa> findByAlunoId(Integer alunoId);

    List<Multa> findByStatus(StatusMulta status);

    // O aluno tem multa pendente? (usado para bloquear novos empréstimos)
    boolean existsByAlunoIdAndStatus(Integer alunoId, StatusMulta status);
}
