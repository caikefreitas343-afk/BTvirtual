package com.biblioteca.service;

import com.biblioteca.entity.Bibliotecario;
import com.biblioteca.entity.TipoUsuario;
import com.biblioteca.repository.BibliotecarioRepository;
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
class BibliotecarioServiceTest {

    @Mock
    private BibliotecarioRepository repository;

    @InjectMocks
    private BibliotecarioService service;

    @Test
    void deveDelegarSalvarListarEBuscar() {
        Bibliotecario bibliotecario = bibliotecario("Original", "original@email.com");
        UUID id = UUID.randomUUID();
        when(repository.save(bibliotecario)).thenReturn(bibliotecario);
        when(repository.findAll()).thenReturn(List.of(bibliotecario));
        when(repository.findById(id)).thenReturn(Optional.of(bibliotecario));

        assertSame(bibliotecario, service.salvar(bibliotecario));
        assertEquals(List.of(bibliotecario), service.listarTodos());
        assertSame(bibliotecario, service.buscarPorId(id));
    }

    @Test
    void deveAtualizarNomeEEmailPreservandoDemaisCampos() {
        UUID id = UUID.randomUUID();
        Bibliotecario existente = bibliotecario("Antigo", "antigo@email.com");
        existente.setId(id);
        existente.setCargo("Chefe");
        existente.setSetor("Acervo");
        Bibliotecario atualizado = bibliotecario("Novo", "novo@email.com");
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Bibliotecario resultado = service.atualizar(id, atualizado);

        assertSame(existente, resultado);
        assertEquals("Novo", existente.getNome());
        assertEquals("novo@email.com", existente.getEmail());
        assertEquals("Chefe", existente.getCargo());
        assertEquals("Acervo", existente.getSetor());
    }

    @Test
    void naoDeveDeletarQuandoNaoEncontrar() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertNotNull(assertThrows(RuntimeException.class, () -> service.deletar(id)));

        verify(repository, never()).deleteById(any());
    }

    @Test
    void deveDeletarQuandoEncontrar() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(bibliotecario("Nome", "email")));

        service.deletar(id);

        verify(repository).deleteById(id);
    }

    private Bibliotecario bibliotecario(String nome, String email) {
        return new Bibliotecario(null, nome, "M-1", email, "senha", "Cargo", "Setor", TipoUsuario.BIBLIOTECARIO);
    }
}
