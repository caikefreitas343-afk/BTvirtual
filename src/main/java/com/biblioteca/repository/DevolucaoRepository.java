package com.biblioteca.repository;

import com.biblioteca.entity.Devolucao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevolucaoRepository extends JpaRepository<Devolucao, Integer> {

    Optional<Devolucao> findByEmprestimoId(Integer emprestimoId);
}
