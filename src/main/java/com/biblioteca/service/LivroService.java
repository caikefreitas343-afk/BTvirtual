package com.biblioteca.service;

import com.biblioteca.entity.Livro;
import com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(UUID id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o ID: " + id));
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }


    public Livro atualizar(UUID id, Livro livroAtualizado) {
        Livro livroExistente = buscarPorId(id);
        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setAutor(livroAtualizado.getAutor());
        livroExistente.setIsbn(livroAtualizado.getIsbn());

        // Adicione outros campos que deseja atualizar
        return livroRepository.save(livroExistente);
    }

    public void deletar(UUID id) {
        // Garante que o livro existe antes de tentar deletar
        Livro livro = buscarPorId(id);
        livroRepository.delete(livro);
    }
    
}
