package com.biblioteca.controller;

import com.biblioteca.dto.ExemplarDTO;
import com.biblioteca.dto.ExemplarRequestDTO;
import com.biblioteca.service.ExemplarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exemplares")
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController(ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }

    @PostMapping
    public ResponseEntity<ExemplarDTO> criar(@RequestBody ExemplarRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exemplarService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ExemplarDTO>> listarTodos() {
        return ResponseEntity.ok(exemplarService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExemplarDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(exemplarService.buscarPorId(id));
    }

    @GetMapping("/tombo/{numeroTombo}")
    public ResponseEntity<ExemplarDTO> buscarPorNumeroTombo(@PathVariable String numeroTombo) {
        return ResponseEntity.ok(exemplarService.buscarPorNumeroTombo(numeroTombo));
    }

    // Todas as cópias de um livro
    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<ExemplarDTO>> listarPorLivro(@PathVariable Integer livroId) {
        return ResponseEntity.ok(exemplarService.listarPorLivro(livroId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExemplarDTO> atualizar(@PathVariable Integer id, @RequestBody ExemplarRequestDTO dto) {
        return ResponseEntity.ok(exemplarService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        exemplarService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
