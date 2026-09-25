package com.biblioteca.service;

import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Reserva;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private LivroRepository livroRepository;
    @InjectMocks
    private ReservaService service;

    @Test
    void deveCriarReservaAtivaComUsuarioLivroEData() {
        UUID usuarioId = UUID.randomUUID();
        UUID livroId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        Livro livro = new Livro();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(livroId)).thenReturn(Optional.of(livro));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
        LocalDateTime antes = LocalDateTime.now();

        Reserva resultado = service.criarReserva(usuarioId, livroId);

        assertSame(usuario, resultado.getUsuario());
        assertSame(livro, resultado.getLivro());
        assertTrue(resultado.isAtiva());
        assertNotNull(resultado.getDataReserva());
        assertNotNull(resultado.getDataValidade());
        assertTrue(resultado.getDataValidade().isAfter(resultado.getDataReserva()));
        assertTrue(!resultado.getDataReserva().isBefore(antes));
        verify(reservaRepository).save(resultado);
    }

    @Test
    void deveRecusarReservaAtivaDuplicada() {
        UUID usuarioId = UUID.randomUUID();
        UUID livroId = UUID.randomUUID();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(new Usuario()));
        when(livroRepository.findById(livroId)).thenReturn(Optional.of(new Livro()));
        when(reservaRepository.findByUsuarioIdAndLivroIdAndAtiva(usuarioId, livroId, true))
                .thenReturn(Optional.of(new Reserva()));

        RuntimeException usuarioException = assertThrows(RuntimeException.class, () -> service.criarReserva(usuarioId, livroId));
        assertNotNull(usuarioException);
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void deveRecusarReservaQuandoUsuarioOuLivroNaoExistirem() {
        UUID usuarioId = UUID.randomUUID();
        UUID livroId = UUID.randomUUID();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        RuntimeException livroException = assertThrows(RuntimeException.class, () -> service.criarReserva(usuarioId, livroId));
        assertNotNull(livroException);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(new Usuario()));
        when(livroRepository.findById(livroId)).thenReturn(Optional.empty());
        assertNotNull(assertThrows(RuntimeException.class, () -> service.criarReserva(usuarioId, livroId)));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void deveListarBuscarECancelarReserva() {
        UUID id = UUID.randomUUID();
        Reserva reserva = new Reserva();
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        assertEquals(List.of(reserva), service.listarTodas());
        assertSame(reserva, service.buscarPorId(id));
        service.cancelarReserva(id);

        assertFalse(reserva.isAtiva());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void deveLancarExcecaoAoBuscarReservaInexistente() {
        UUID id = UUID.randomUUID();
        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException buscaException = assertThrows(RuntimeException.class, () -> service.buscarPorId(id));
        RuntimeException cancelamentoException = assertThrows(RuntimeException.class, () -> service.cancelarReserva(id));
        assertNotNull(buscaException);
        assertNotNull(cancelamentoException);
        verify(reservaRepository, never()).save(any());
    }
}
