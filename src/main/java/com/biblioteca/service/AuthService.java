package com.biblioteca.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.dto.LoginRequestDTO;
import com.biblioteca.dto.LoginResponseDTO;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoService alunoService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       AlunoService alunoService,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.alunoService = alunoService;
        this.passwordEncoder = passwordEncoder;
    }

    public AlunoDTO cadastrarAluno(CadastroAlunoDTO dto) {
        return alunoService.criar(dto);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        if (dto == null || dto.getIdentificador() == null || dto.getIdentificador().isBlank()) {
            throw new IllegalArgumentException("Usuário ou e-mail é obrigatório.");
        }

        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        String identificador = dto.getIdentificador();
        Optional<Usuario> usuarioPorEmail = usuarioRepository.findByEmail(identificador);
        Optional<Usuario> usuarioPorMatricula = usuarioRepository.findByMatricula(identificador);

        Usuario usuario = usuarioPorEmail.or(() -> usuarioPorMatricula)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        if (usuario.getSenha() == null || !passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        return new LoginResponseDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().name() : "USUARIO",
                "Login realizado com sucesso."
        );
    }
}
