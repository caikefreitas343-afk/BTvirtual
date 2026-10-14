package com.biblioteca.service;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.dto.LoginRequestDTO;
import com.biblioteca.dto.LoginResponseDTO;
import com.biblioteca.entity.Aluno;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.AlunoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AlunoRepository alunoRepository, UsuarioRepository usuarioRepository) {
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public AlunoDTO cadastrarAluno(CadastroAlunoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do aluno são obrigatórios.");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }

        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        if (alunoRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Aluno aluno = new Aluno(
                null,
                dto.getNome(),
                dto.getMatricula(),
                dto.getEmail(),
                dto.getSenha(),
                dto.getTurma(),
                dto.getSerie()
        );

        Aluno salvo = alunoRepository.save(aluno);
        return new AlunoDTO(salvo);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        if (dto == null || dto.getIdentificador() == null || dto.getIdentificador().isBlank()) {
            throw new IllegalArgumentException("Usuário ou e-mail é obrigatório.");
        }

        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        String identificador = dto.getIdentificador();
        String senha = dto.getSenha();

        Optional<Usuario> usuarioPorEmail = usuarioRepository.findByEmail(identificador);
        Optional<Usuario> usuarioPorMatricula = usuarioRepository.findByMatricula(identificador);

        Usuario usuario = usuarioPorEmail.or(() -> usuarioPorMatricula)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        if (!usuario.getSenha().equals(senha)) {
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
