package com.biblioteca.service;

import com.biblioteca.entity.Aluno;
import com.biblioteca.repository.AlunoRepository;
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
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService service;

    @Test
    void deveDelegarSalvarListarEBuscarPorMatricula() {
        Aluno aluno = new Aluno();
        UUID id = UUID.randomUUID();
        when(alunoRepository.save(aluno)).thenReturn(aluno);
        when(alunoRepository.findAll()).thenReturn(List.of(aluno));
        when(alunoRepository.findByMatricula("A-1")).thenReturn(Optional.of(aluno));

        assertSame(aluno, service.salvar(aluno));
        assertEquals(List.of(aluno), service.listarTodos());
        assertSame(aluno, service.buscarPorMatricula("A-1"));
        verify(alunoRepository).save(aluno);
        verify(alunoRepository).findAll();
        verify(alunoRepository).findByMatricula("A-1");
        assertNotNull(id);
    }

    @Test
    void deveBuscarPorIdEAtualizarSomenteCamposPermitidos() {
        UUID id = UUID.randomUUID();
        Aluno existente = new Aluno();
        existente.setId(id);
        existente.setSenha("senha-original");
        Aluno atualizado = new Aluno();
        atualizado.setNome("Novo Nome");
        atualizado.setEmail("novo@email.com");
        atualizado.setMatricula("A-2");
        when(alunoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(alunoRepository.save(existente)).thenReturn(existente);

        Aluno resultado = service.atualizar(id, atualizado);

        assertSame(existente, resultado);
        assertEquals("Novo Nome", existente.getNome());
        assertEquals("novo@email.com", existente.getEmail());
        assertEquals("A-2", existente.getMatricula());
        assertEquals("senha-original", existente.getSenha());
        verify(alunoRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontrado() {
        UUID id = UUID.randomUUID();
        when(alunoRepository.findById(id)).thenReturn(Optional.empty());
        when(alunoRepository.findByMatricula("inexistente")).thenReturn(Optional.empty());

        RuntimeException idException = assertThrows(RuntimeException.class, () -> service.buscarPorId(id));
        RuntimeException matriculaException = assertThrows(RuntimeException.class, () -> service.buscarPorMatricula("inexistente"));
        assertNotNull(idException);
        assertNotNull(matriculaException);
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void deveDeletarPorId() {
        UUID id = UUID.randomUUID();
        when(alunoRepository.findById(id)).thenReturn(Optional.of(new Aluno()));

        service.deletar(id);

        verify(alunoRepository).deleteById(id);
    }
}
