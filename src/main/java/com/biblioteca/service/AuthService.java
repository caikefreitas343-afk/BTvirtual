package com.biblioteca.service;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.dto.LoginRequestDTO;
import com.biblioteca.dto.LoginResponseDTO;
import com.biblioteca.entity.Aluno;
import com.biblioteca.entity.Funcionario;
import com.biblioteca.entity.TipoUsuario;
import com.biblioteca.repository.AlunoRepository;
import com.biblioteca.repository.FuncionarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private static final String MENSAGEM_LOGIN_INVALIDO = "Usuário ou senha inválidos";

    private final AlunoRepository alunoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AlunoService alunoService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AlunoRepository alunoRepository,
                       FuncionarioRepository funcionarioRepository,
                       AlunoService alunoService,
                       PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.alunoService = alunoService;
        this.passwordEncoder = passwordEncoder;
    }

    // Cadastro público: só cria ALUNO (a regra fica no AlunoService)
    public AlunoDTO cadastrarAluno(CadastroAlunoDTO dto) {
        return alunoService.criar(dto);
    }

    // Login: procura primeiro entre os alunos e depois entre os funcionários (bibliotecários)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        if (dto == null || dto.getIdentificador() == null || dto.getIdentificador().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o usuário ou o e-mail");
        }
        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a senha");
        }

        String identificador = dto.getIdentificador().trim();

        Optional<Aluno> aluno = alunoRepository.findByMatricula(identificador)
                .or(() -> alunoRepository.findByEmail(identificador));
        if (aluno.isPresent()) {
            Aluno a = aluno.get();
            conferirSenha(dto.getSenha(), a.getSenha());
            return new LoginResponseDTO(a.getNomeCompleto(), a.getEmail(),
                    TipoUsuario.ALUNO.name(), "Login realizado com sucesso.");
        }

        Optional<Funcionario> funcionario = funcionarioRepository.findByMatriculaFuncional(identificador)
                .or(() -> funcionarioRepository.findByEmail(identificador));
        if (funcionario.isPresent()) {
            Funcionario f = funcionario.get();
            conferirSenha(dto.getSenha(), f.getSenha());
            return new LoginResponseDTO(f.getNome(), f.getEmail(),
                    TipoUsuario.BIBLIOTECARIO.name(), "Login realizado com sucesso.");
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MENSAGEM_LOGIN_INVALIDO);
    }

    private void conferirSenha(String senhaDigitada, String senhaSalva) {
        if (senhaSalva == null || !passwordEncoder.matches(senhaDigitada, senhaSalva)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MENSAGEM_LOGIN_INVALIDO);
        }
    }
}
