package com.biblioteca.controller;

import com.biblioteca.dto.AutorDTO;
import com.biblioteca.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<AutorDTO> criar(@RequestBody AutorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }
}
