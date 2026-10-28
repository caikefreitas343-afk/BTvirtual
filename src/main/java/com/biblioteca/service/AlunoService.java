package com.biblioteca.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.AtualizarAlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.entity.Aluno;
import com.biblioteca.repository.AlunoRepository;
import com.biblioteca.repository.UsuarioRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository,
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public AlunoDTO criar(CadastroAlunoDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do aluno são obrigatórios");
        }
        validarDados(dto.getNome(), dto.getMatricula(), dto.getEmail());
        validarSenha(dto.getSenha());
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (usuarioRepository.findByMatricula(dto.getMatricula()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada");
        }

        Aluno aluno = new Aluno(
                null,
                dto.getNome(),
                dto.getMatricula(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()),
                dto.getTurma(),
                dto.getSerie()
        );
        return new AlunoDTO(alunoRepository.save(aluno));
    }

    // READ
    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll().stream().map(AlunoDTO::new).toList();
    }

    public AlunoDTO buscarPorId(UUID id) {
        return new AlunoDTO(buscarEntidade(id));
    }

    public AlunoDTO buscarPorMatricula(String matricula) {
        Aluno aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com a matrícula: " + matricula));
        return new AlunoDTO(aluno);
    }

    // UPDATE
    public AlunoDTO atualizar(UUID id, AtualizarAlunoDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do aluno são obrigatórios");
        }
        validarDados(dto.nome(), dto.matricula(), dto.email());
        Aluno aluno = buscarEntidade(id);

        if (!aluno.getEmail().equals(dto.email())
                && usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (!aluno.getMatricula().equals(dto.matricula())
                && usuarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada");
        }

        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setMatricula(dto.matricula());
        aluno.setTurma(dto.turma());
        aluno.setSerie(dto.serie());

        return new AlunoDTO(alunoRepository.save(aluno));
    }

    // DELETE
    public void deletar(UUID id) {
        alunoRepository.delete(buscarEntidade(id));
    }

    private Aluno buscarEntidade(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com o ID: " + id));
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A senha precisa ter pelo menos 6 caracteres");
        }
    }

    private void validarDados(String nome, String matricula, String email) {
        if (nome == null || nome.isBlank()
                || matricula == null || matricula.isBlank()
                || email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nome, matrícula e e-mail são obrigatórios");
        }
    }
}