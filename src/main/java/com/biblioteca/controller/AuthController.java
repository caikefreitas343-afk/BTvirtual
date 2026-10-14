package com.biblioteca.controller;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.dto.LoginRequestDTO;
import com.biblioteca.dto.LoginResponseDTO;
import com.biblioteca.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<AlunoDTO> cadastrar(@RequestBody CadastroAlunoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.cadastrarAluno(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }
}
