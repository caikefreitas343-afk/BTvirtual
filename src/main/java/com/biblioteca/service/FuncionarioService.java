package com.biblioteca.service;

import com.biblioteca.entity.Funcionario;
import com.biblioteca.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario salvar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário é obrigatório.");
        }
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(UUID id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));
    }

    public Funcionario atualizar(UUID id, Funcionario funcionarioAtualizado) {
        if (funcionarioAtualizado == null) {
            throw new IllegalArgumentException("Dados do funcionário são obrigatórios.");
        }

        Funcionario funcionarioExistente = buscarPorId(id);
        funcionarioExistente.setNome(funcionarioAtualizado.getNome());
        funcionarioExistente.setMatricula(funcionarioAtualizado.getMatricula());
        funcionarioExistente.setEmail(funcionarioAtualizado.getEmail());
        funcionarioExistente.setCargo(funcionarioAtualizado.getCargo());
        funcionarioExistente.setSetor(funcionarioAtualizado.getSetor());

        return funcionarioRepository.save(funcionarioExistente);
    }

    public void deletar(UUID id) {
        // Garante que o funcionário existe antes de tentar deletar
        buscarPorId(id);
        funcionarioRepository.deleteById(id);
    }
}
