package com.biblioteca.service;

import com.biblioteca.entity.Livro;
import com.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class LivroServiceTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroService service;

    @Test
    void deveDelegarOperacoesDeConsulta() {
        Livro livro = livro("Java", "Autor");
        UUID id = UUID.randomUUID();
        when(repository.save(livro)).thenReturn(livro);
        when(repository.findAll()).thenReturn(List.of(livro));
        when(repository.findById(id)).thenReturn(Optional.of(livro));
        when(repository.findByTituloContainingIgnoreCase("java")).thenReturn(List.of(livro));
        when(repository.findByAutorContainingIgnoreCase("autor")).thenReturn(List.of(livro));

        assertSame(livro, service.salvar(livro));
        assertEquals(List.of(livro), service.listarTodos());
        assertSame(livro, service.buscarPorId(id));
        assertEquals(List.of(livro), service.buscarPorTitulo("java"));
        assertEquals(List.of(livro), service.buscarPorAutor("autor"));
        verify(repository).findByTituloContainingIgnoreCase("java");
        verify(repository).findByAutorContainingIgnoreCase("autor");
    }

    @Test
    void deveAtualizarTituloAutorEIsbnPreservandoEstoque() {
        UUID id = UUID.randomUUID();
        Livro existente = livro("Antigo", "Autor antigo");
        existente.setId(id);
        existente.setQuantidadeEstoque(10);
        existente.setQuantidadeDisponivel(4);
        Livro atualizado = livro("Novo", "Autor novo");
        atualizado.setIsbn("novo-isbn");
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Livro resultado = service.atualizar(id, atualizado);

        assertSame(existente, resultado);
        assertEquals("Novo", existente.getTitulo());
        assertEquals("Autor novo", existente.getAutor());
        assertEquals("novo-isbn", existente.getIsbn());
        assertEquals(10, existente.getQuantidadeEstoque());
        assertEquals(4, existente.getQuantidadeDisponivel());
    }

    @Test
    void deveImpedirExclusaoEAtualizacaoDeLivroInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertNotNull(assertThrows(RuntimeException.class, () -> service.buscarPorId(id)));
        assertNotNull(assertThrows(RuntimeException.class, () -> service.atualizar(id, new Livro())));
        assertNotNull(assertThrows(RuntimeException.class, () -> service.deletar(id)));
        verify(repository, never()).deleteById(any());
        verify(repository, never()).save(any());
    }

    private Livro livro(String titulo, String autor) {
        return new Livro(titulo, autor, "isbn", 2025, "tecnologia", 10, 5, "codigo", null);
    }
}
