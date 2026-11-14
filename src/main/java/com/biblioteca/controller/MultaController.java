package com.biblioteca.controller;

import com.biblioteca.dto.MultaDTO;
import com.biblioteca.dto.MultaRequestDTO;
import com.biblioteca.service.MultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @PostMapping
    public ResponseEntity<MultaDTO> criar(@RequestBody MultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(multaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MultaDTO>> listarTodas() {
        return ResponseEntity.ok(multaService.listarTodas());
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<MultaDTO>> listarPorAluno(@PathVariable Integer alunoId) {
        return ResponseEntity.ok(multaService.listarPorAluno(alunoId));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<MultaDTO> pagar(@PathVariable Integer id) {
        return ResponseEntity.ok(multaService.pagar(id));
    }
}
