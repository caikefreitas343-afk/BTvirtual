package com.biblioteca.service;

import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Aluno;
import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;
    @Mock
    private LivroRepository livroRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private EmprestimoService service;

    @Test
    void deveRealizarEmprestimoEDecrementarEstoque() {
        UUID usuarioId = UUID.randomUUID();
        UUID livroId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        Livro livro = livroComEstoque(3);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(livroRepository.findByIdForUpdate(livroId)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        LocalDateTime antes = LocalDateTime.now();

        Emprestimo resultado = service.realizarEmprestimo(usuarioId, livroId);

        LocalDateTime depois = LocalDateTime.now();
        assertSame(usuario, resultado.getUsuario());
        assertSame(livro, resultado.getLivro());
        assertEquals(2, livro.getQuantidadeDisponivel());
        assertEquals(com.biblioteca.entity.StatusEmprestimo.ATIVO, resultado.getStatus());
        assertNotNull(resultado.getDataEmprestimo());
        assertTrue(!resultado.getDataEmprestimo().isBefore(antes) && !resultado.getDataEmprestimo().isAfter(depois));
        assertTrue(Duration.between(resultado.getDataEmprestimo(), resultado.getDataDevolucaoPrevista()).toDays() >= 7);
        verify(livroRepository).save(livro);
        verify(emprestimoRepository).save(resultado);
    }

    @Test
    void deveRecusarEmprestimoDeAlunoBloqueado() {
        UUID usuarioId = UUID.randomUUID();
        Aluno aluno = new Aluno();
        aluno.setBloqueado(true);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(aluno));

        assertNotNull(assertThrows(RuntimeException.class, () -> service.realizarEmprestimo(usuarioId, UUID.randomUUID())));
        verifyNoInteractions(livroRepository, emprestimoRepository);
    }

    @Test
    void deveRecusarEmprestimoQuandoUsuarioLivroOuEstoqueForemInvalidos() {
        UUID usuarioId = UUID.randomUUID();
        UUID livroId = UUID.randomUUID();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        assertNotNull(assertThrows(RuntimeException.class, () -> service.realizarEmprestimo(usuarioId, livroId)));

        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(livroRepository.findByIdForUpdate(livroId)).thenReturn(Optional.empty());
        assertNotNull(assertThrows(RuntimeException.class, () -> service.realizarEmprestimo(usuarioId, livroId)));

        when(livroRepository.findByIdForUpdate(livroId)).thenReturn(Optional.of(livroComEstoque(0)));
        assertNotNull(assertThrows(RuntimeException.class, () -> service.realizarEmprestimo(usuarioId, livroId)));
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    void deveRealizarDevolucaoEIncrementarEstoque() {
        UUID id = UUID.randomUUID();
        Livro livro = livroComEstoque(1);
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setStatus(com.biblioteca.entity.StatusEmprestimo.ATIVO);
        when(emprestimoRepository.findByIdForUpdate(id)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);
        LocalDateTime antes = LocalDateTime.now();

        Emprestimo resultado = service.realizarDevolucao(id);

        assertSame(emprestimo, resultado);
        assertEquals(2, livro.getQuantidadeDisponivel());
        assertNotNull(emprestimo.getDataDevolucaoEfetiva());
        assertTrue(!emprestimo.getDataDevolucaoEfetiva().isBefore(antes));
        verify(livroRepository).save(livro);
        verify(emprestimoRepository).save(emprestimo);
    }

    @Test
    void naoDeveDevolverEmprestimoMaisDeUmaVez() {
        UUID id = UUID.randomUUID();
        Livro livro = livroComEstoque(1);
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setStatus(com.biblioteca.entity.StatusEmprestimo.ATIVO);
        when(emprestimoRepository.findByIdForUpdate(id)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

        service.realizarDevolucao(id);
        int estoqueAposPrimeiraDevolucao = livro.getQuantidadeDisponivel();

        assertNotNull(assertThrows(RuntimeException.class, () -> service.realizarDevolucao(id)));
        assertEquals(estoqueAposPrimeiraDevolucao, livro.getQuantidadeDisponivel());
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    void deveRenovarAteTresVezesERecusarAQuarta() {
        UUID id = UUID.randomUUID();
        LocalDateTime prevista = LocalDateTime.now().plusDays(7);
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setDataDevolucaoPrevista(prevista);
        emprestimo.setQuantidadeRenovacoes(2);
        emprestimo.setStatus(com.biblioteca.entity.StatusEmprestimo.ATIVO);
        when(emprestimoRepository.findByIdForUpdate(id)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

        service.renovarEmprestimo(id);

        assertEquals(3, emprestimo.getQuantidadeRenovacoes());
        assertEquals(prevista.plusDays(7), emprestimo.getDataDevolucaoPrevista());
        emprestimo.setQuantidadeRenovacoes(3);
        assertNotNull(assertThrows(RuntimeException.class, () -> service.renovarEmprestimo(id)));
        verify(emprestimoRepository, times(1)).save(emprestimo);
    }

    @Test
    void deveDelegarListagemEBusca() {
        UUID id = UUID.randomUUID();
        Emprestimo emprestimo = new Emprestimo();
        when(emprestimoRepository.findAll()).thenReturn(List.of(emprestimo));
        when(emprestimoRepository.findById(id)).thenReturn(Optional.of(emprestimo));

        assertEquals(List.of(emprestimo), service.listarTodos());
        assertSame(emprestimo, service.buscarPorId(id));
        when(emprestimoRepository.findById(id)).thenReturn(Optional.empty());
        assertNotNull(assertThrows(RuntimeException.class, () -> service.buscarPorId(id)));
    }

    private Livro livroComEstoque(int quantidade) {
        Livro livro = new Livro();
        livro.setQuantidadeDisponivel(quantidade);
        return livro;
    }
}
