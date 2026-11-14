package com.biblioteca.controller;

import com.biblioteca.dto.EditoraDTO;
import com.biblioteca.service.EditoraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @PostMapping
    public ResponseEntity<EditoraDTO> criar(@RequestBody EditoraDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editoraService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EditoraDTO>> listarTodas() {
        return ResponseEntity.ok(editoraService.listarTodas());
    }
}
