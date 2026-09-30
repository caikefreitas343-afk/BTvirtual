package com.biblioteca.service;

import com.biblioteca.entity.Aluno;
import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Reserva;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional
    public Reserva criarReserva(UUID usuarioId, UUID livroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (usuario instanceof Aluno aluno && aluno.isBloqueado()) {
            throw new RuntimeException("Usuário bloqueado para reservas.");
        }

        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        if (reservaRepository.findByUsuarioIdAndLivroIdAndAtiva(usuarioId, livroId, true).isPresent()) {
            throw new RuntimeException("Usuário já possui uma reserva ativa para este livro.");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setLivro(livro);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setDataValidade(LocalDateTime.now().plusDays(7));
        reserva.setAtiva(true); // Define a reserva como ativa

        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(UUID id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada com o ID: " + id));
    }

    @Transactional
    public void cancelarReserva(UUID id) {
        Reserva reserva = buscarPorId(id);
        reserva.setAtiva(false); // Marca a reserva como inativa
        reservaRepository.save(reserva);
    }
}