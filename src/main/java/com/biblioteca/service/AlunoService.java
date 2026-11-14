package com.biblioteca.service;

import com.biblioteca.dto.AlunoDTO;
import com.biblioteca.dto.AtualizarAlunoDTO;
import com.biblioteca.dto.CadastroAlunoDTO;
import com.biblioteca.entity.Aluno;
import com.biblioteca.entity.StatusMulta;
import com.biblioteca.entity.Turma;
import com.biblioteca.repository.AlunoRepository;
import com.biblioteca.repository.MultaRepository;
import com.biblioteca.repository.TurmaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final MultaRepository multaRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository,
                        TurmaRepository turmaRepository,
                        MultaRepository multaRepository,
                        PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.multaRepository = multaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public AlunoDTO criar(CadastroAlunoDTO dto) {
        validarSenha(dto.senha());
        if (alunoRepository.existsByMatricula(dto.matricula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada");
        }
        if (alunoRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        Aluno aluno = new Aluno();
        aluno.setPrenome(dto.prenome());
        aluno.setSobrenome(dto.sobrenome());
        aluno.setMatricula(dto.matricula());
        aluno.setEmail(dto.email());
        aluno.setSenha(passwordEncoder.encode(dto.senha())); // salva só o hash
        aluno.setTurma(buscarTurma(dto.idTurma()));

        return paraDTO(alunoRepository.save(aluno));
    }

    // READ
    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll().stream().map(this::paraDTO).toList();
    }

    public AlunoDTO buscarPorId(Integer id) {
        return paraDTO(buscarEntidade(id));
    }

    public AlunoDTO buscarPorMatricula(String matricula) {
        Aluno aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com a matrícula: " + matricula));
        return paraDTO(aluno);
    }

    // UPDATE
    public AlunoDTO atualizar(Integer id, AtualizarAlunoDTO dto) {
        Aluno aluno = buscarEntidade(id);

        if (!aluno.getMatricula().equals(dto.matricula()) && alunoRepository.existsByMatricula(dto.matricula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada");
        }
        if (dto.email() != null && !dto.email().equals(aluno.getEmail()) && alunoRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        aluno.setPrenome(dto.prenome());
        aluno.setSobrenome(dto.sobrenome());
        aluno.setMatricula(dto.matricula());
        aluno.setEmail(dto.email());
        aluno.setTurma(buscarTurma(dto.idTurma()));

        if (dto.novaSenha() != null && !dto.novaSenha().isBlank()) {
            validarSenha(dto.novaSenha());
            aluno.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }
        return paraDTO(alunoRepository.save(aluno));
    }

    // DELETE
    public void deletar(Integer id) {
        alunoRepository.delete(buscarEntidade(id));
    }

    private Aluno buscarEntidade(Integer id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com o ID: " + id));
    }

    private Turma buscarTurma(Integer idTurma) {
        if (idTurma == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escolha a turma do aluno");
        }
        return turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada"));
    }

    // O aluno fica bloqueado enquanto tiver multa pendente (não existe coluna "bloqueado" no banco)
    private AlunoDTO paraDTO(Aluno aluno) {
        boolean bloqueado = multaRepository.existsByAlunoIdAndStatus(aluno.getId(), StatusMulta.PENDENTE);
        return AlunoDTO.from(aluno, bloqueado);
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A senha precisa ter pelo menos 6 caracteres");
        }
    }
}
