package com.biblioteca.repository;

import com.biblioteca.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    Optional<Funcionario> findByMatriculaFuncional(String matriculaFuncional);

    Optional<Funcionario> findByEmail(String email);

    boolean existsByMatriculaFuncional(String matriculaFuncional);

    boolean existsByEmail(String email);
}
