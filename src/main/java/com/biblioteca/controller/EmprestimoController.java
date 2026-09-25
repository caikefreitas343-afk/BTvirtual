package com.biblioteca.controller;

import com.biblioteca.entity.Emprestimo;
import com.biblioteca.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    
    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping 
    public ResponseEntity<Emprestimo> realizarEmprestimo(@RequestParam UUID idUsuario, @RequestParam UUID livroId) {
        Emprestimo novoEmprestimo = emprestimoService.realizarEmprestimo(idUsuario, livroId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEmprestimo);
    }

    @GetMapping 
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        return ResponseEntity.ok(emprestimoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(emprestimoService.buscarPorId(id));
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Emprestimo> realizarDevolucao(@PathVariable UUID id) {
        return ResponseEntity.ok(emprestimoService.realizarDevolucao(id));
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<Emprestimo> renovarEmprestimo(@PathVariable UUID id) {
        return ResponseEntity.ok(emprestimoService.renovarEmprestimo(id));
    }
}
