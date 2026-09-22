package com.biblioteca.controller;

import com.biblioteca.entity.Livro;
import com.biblioteca.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/livros")

public class LivroController {
    
    public final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody Livro livro) {
        Livro novoLivro = livroService.salvar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));

    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Livro>> buscarPorTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(livroService.buscarPorTitulo(titulo));
    
    }
    
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Livro>> buscarPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(livroService.buscarPorAutor(autor));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable UUID id, @RequestBody Livro livro) {
        return ResponseEntity.ok(livroService.atualizar(id, livro));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();

    }
}
