package com.biblioteca.service;

import com.biblioteca.dto.FuncionarioDTO;
import com.biblioteca.dto.FuncionarioRequestDTO;
import com.biblioteca.entity.Funcionario;
import com.biblioteca.repository.FuncionarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public FuncionarioDTO criar(FuncionarioRequestDTO dto) {
        validarSenha(dto.senha());
        if (funcionarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (funcionarioRepository.existsByMatriculaFuncional(dto.matriculaFuncional())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula funcional já cadastrada");
        }

        Funcionario funcionario = new Funcionario();
        preencher(funcionario, dto);
        funcionario.setSenha(passwordEncoder.encode(dto.senha()));
        return FuncionarioDTO.from(funcionarioRepository.save(funcionario));
    }

    // READ
    public List<FuncionarioDTO> listarTodos() {
        return funcionarioRepository.findAll().stream().map(FuncionarioDTO::from).toList();
    }

    public FuncionarioDTO buscarPorId(Integer id) {
        return FuncionarioDTO.from(buscarEntidade(id));
    }

    // UPDATE
    public FuncionarioDTO atualizar(Integer id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = buscarEntidade(id);

        if (!funcionario.getEmail().equals(dto.email()) && funcionarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (!funcionario.getMatriculaFuncional().equals(dto.matriculaFuncional())
                && funcionarioRepository.existsByMatriculaFuncional(dto.matriculaFuncional())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula funcional já cadastrada");
        }

        preencher(funcionario, dto);
        if (dto.senha() != null && !dto.senha().isBlank()) {
            validarSenha(dto.senha());
            funcionario.setSenha(passwordEncoder.encode(dto.senha()));
        }
        return FuncionarioDTO.from(funcionarioRepository.save(funcionario));
    }

    // DELETE
    public void deletar(Integer id) {
        funcionarioRepository.delete(buscarEntidade(id));
    }

    private void preencher(Funcionario funcionario, FuncionarioRequestDTO dto) {
        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setDataAdmissao(dto.dataAdmissao() != null ? dto.dataAdmissao() : LocalDate.now());
        funcionario.setCargo(dto.cargo());
        funcionario.setMatriculaFuncional(dto.matriculaFuncional());
    }

    private Funcionario buscarEntidade(Integer id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário não encontrado com o ID: " + id));
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A senha precisa ter pelo menos 6 caracteres");
        }
    }
}
