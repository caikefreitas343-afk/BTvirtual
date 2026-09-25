package com.biblioteca.service;

import com.biblioteca.entity.Bibliotecario;
import com.biblioteca.repository.BibliotecarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class BibliotecarioService {

    private final BibliotecarioRepository bibliotecarioRepository;

    public BibliotecarioService(BibliotecarioRepository bibliotecarioRepository) {
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    public Bibliotecario salvar(Bibliotecario bibliotecario) {
        if (bibliotecario == null) {
            throw new IllegalArgumentException("Bibliotecário é obrigatório.");
        }
        return bibliotecarioRepository.save(bibliotecario);
    }

    public List<Bibliotecario> listarTodos() {
        return bibliotecarioRepository.findAll();
    }

    public Bibliotecario buscarPorId(UUID id) {
        return bibliotecarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bibliotecário não encontrado com o ID: " + id));
    }

    public Bibliotecario atualizar(UUID id, Bibliotecario bibliotecarioAtualizado) {
        Bibliotecario bibliotecarioExistente = buscarPorId(id);
        
        bibliotecarioExistente.setNome(bibliotecarioAtualizado.getNome());
        bibliotecarioExistente.setEmail(bibliotecarioAtualizado.getEmail());
        // Adicione outros campos que deseja atualizar
        
        return bibliotecarioRepository.save(bibliotecarioExistente);
    }

    public void deletar(UUID id) {
        // Garante que o bibliotecário existe antes de tentar deletar
        buscarPorId(id);
        bibliotecarioRepository.deleteById(id);
    }
}

