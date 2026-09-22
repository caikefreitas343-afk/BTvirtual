package com.biblioteca.service;

import com.biblioteca.entity.*;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Emprestimo realizarEmprestimo(UUID usuarioId, UUID livroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro indisponível para empréstimo.");
        }

        // Diminui o estoque do livro e salva
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        // Cria o empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(7)); // Prazo de 7 dias
        // O status é setado com o seu Enum, assumindo que exista um "ATIVO"
        // emprestimo.setStatus(StatusEmprestimo.ATIVO); 

        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo realizarDevolucao(UUID id) {
        Emprestimo emprestimo = buscarPorId(id);
        
        emprestimo.setDataDevolucaoEfetiva(LocalDateTime.now());
        // emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        // Devolve o livro para o estoque
        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo renovarEmprestimo(UUID id) {
        Emprestimo emprestimo = buscarPorId(id);
        
        if (emprestimo.getQuantidadeRenovacoes() >= 3) {
            throw new RuntimeException("Limite de renovações atingido.");
        }

        emprestimo.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista().plusDays(7));
        emprestimo.setQuantidadeRenovacoes(emprestimo.getQuantidadeRenovacoes() + 1);
        
        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscarPorId(UUID id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));
    }
}