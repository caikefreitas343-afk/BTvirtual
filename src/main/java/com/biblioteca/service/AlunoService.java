package com.biblioteca.service;

import com.biblioteca.entity.Aluno;
import com.biblioteca.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * AlunoService
 */
@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno buscarPorId(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
    }

    public Aluno buscarPorMatricula(String matricula) {
        return alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com a matrícula: " + matricula));
    }

    public Aluno atualizar(UUID id, Aluno alunoAtualizado) {
        Aluno alunoExistente = buscarPorId(id);
        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setEmail(alunoAtualizado.getEmail());
        alunoExistente.setMatricula(alunoAtualizado.getMatricula());
        // Adicione outros campos que deseja atualizar
        return alunoRepository.save(alunoExistente);
    }

    public void deletar(UUID id) {
        buscarPorId(id);
        alunoRepository.deleteById(id);
    }
}