package com.biblioteca.service;

import com.biblioteca.entity.*;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Emprestimo realizarEmprestimo(UUID usuarioId, UUID livroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (usuario instanceof Aluno aluno && aluno.isBloqueado()) {
            throw new RuntimeException("Usuário bloqueado para empréstimos.");
        }
        
        Livro livro = livroRepository.findByIdForUpdate(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        if (livro.getQuantidadeDisponivel() == null || livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro indisponível para empréstimo.");
        }

        // Diminui o estoque do livro e salva
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        atualizarStatusLivro(livro);
        livroRepository.save(livro);

        // Cria o empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(7)); // Prazo de 7 dias: sujeito a alterações
        emprestimo.setQuantidadeRenovacoes(0);
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo realizarDevolucao(UUID id) {
        Emprestimo emprestimo = emprestimoRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));
        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO
                && emprestimo.getStatus() != StatusEmprestimo.ATRASADO) {
            throw new RuntimeException("Empréstimo não está ativo.");
        }
        if (emprestimo.getDataDevolucaoEfetiva() != null || emprestimo.getLivro() == null) {
            throw new RuntimeException("Empréstimo já devolvido ou sem livro associado.");
        }

        emprestimo.setDataDevolucaoEfetiva(LocalDateTime.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        // Devolve o livro para o estoque
        Livro livro = emprestimo.getLivro();
        if (livro.getQuantidadeDisponivel() == null) {
            throw new RuntimeException("Estoque do livro é inválido.");
        }
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        atualizarStatusLivro(livro);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo renovarEmprestimo(UUID id) {
        Emprestimo emprestimo = emprestimoRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));
        
        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO
                || emprestimo.getDataDevolucaoEfetiva() != null
                || emprestimo.getDataDevolucaoPrevista() == null) {
            throw new RuntimeException("Empréstimo não pode ser renovado.");
        }
        if (emprestimo.getQuantidadeRenovacoes() == null || emprestimo.getQuantidadeRenovacoes() >= 3) {
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

    private void atualizarStatusLivro(Livro livro) {
        livro.setStatus(livro.getQuantidadeDisponivel() > 0
                ? StatusLivro.DISPONIVEL : StatusLivro.EMPRESTADO);
    }
}