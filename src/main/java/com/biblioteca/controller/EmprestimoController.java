package com.biblioteca.controller;

import com.biblioteca.dto.DevolucaoRequestDTO;
import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.RealizarEmprestimoDTO;
import com.biblioteca.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<EmprestimoDTO> realizarEmprestimo(@RequestBody RealizarEmprestimoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.realizarEmprestimo(dto));
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> listarTodos() {
        return ResponseEntity.ok(emprestimoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(emprestimoService.buscarPorId(id));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<EmprestimoDTO>> listarPorAluno(@PathVariable Integer alunoId) {
        return ResponseEntity.ok(emprestimoService.listarPorAluno(alunoId));
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoDTO> realizarDevolucao(@PathVariable Integer id,
                                                           @RequestBody DevolucaoRequestDTO dto) {
        return ResponseEntity.ok(emprestimoService.realizarDevolucao(id, dto));
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<EmprestimoDTO> renovarEmprestimo(@PathVariable Integer id) {
        return ResponseEntity.ok(emprestimoService.renovarEmprestimo(id));
    }
}
