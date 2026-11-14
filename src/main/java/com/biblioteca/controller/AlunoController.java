package com.biblioteca.controller;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.AtualizarAlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> criar(@RequestBody CadastroAlunoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AlunoDTO> buscarPorMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(alunoService.buscarPorMatricula(matricula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> atualizar(@PathVariable Integer id, @RequestBody AtualizarAlunoDTO dto) {
        return ResponseEntity.ok(alunoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
