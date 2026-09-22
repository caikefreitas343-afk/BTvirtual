package com.biblioteca.controller;

import com.biblioteca.entity.Bibliotecario;
import com.biblioteca.service.BibliotecarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bibliotecarios")
public class BibliotecarioController {
    
    private final BibliotecarioService bibliotecarioService;

    public BibliotecarioController(BibliotecarioService bibliotecarioService) {
        this.bibliotecarioService = bibliotecarioService;
    }

    @PostMapping  
    public ResponseEntity<Bibliotecario> criar(@RequestBody Bibliotecario bibliotecario) {
        Bibliotecario novoBibliotecario = bibliotecarioService.salvar(bibliotecario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoBibliotecario);
    }

    @GetMapping 
    public ResponseEntity<List<Bibliotecario>> listarTodos() {
        return ResponseEntity.ok(bibliotecarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bibliotecario> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(bibliotecarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bibliotecario> atualizar(@PathVariable UUID id, @RequestBody Bibliotecario bibliotecario) {
        return ResponseEntity.ok(bibliotecarioService.atualizar(id, bibliotecario));
    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        bibliotecarioService.deletar(id);
        return ResponseEntity.noContent().build(); 
    
    }
}
