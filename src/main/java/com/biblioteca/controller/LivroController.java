package com.biblioteca.controller;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroDTO> criar(@RequestBody LivroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<LivroDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroDTO>> buscarPorTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(livroService.buscarPorTitulo(titulo));
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LivroDTO>> buscarPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(livroService.buscarPorAutor(autor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroDTO> atualizar(@PathVariable Integer id, @RequestBody LivroRequestDTO dto) {
        return ResponseEntity.ok(livroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
