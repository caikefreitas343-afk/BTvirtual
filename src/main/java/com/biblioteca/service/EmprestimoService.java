package com.biblioteca.service;

import com.biblioteca.dto.DevolucaoRequestDTO;
import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.RealizarEmprestimoDTO;
import com.biblioteca.entity.*;
import com.biblioteca.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class EmprestimoService {

    // Regras da biblioteca (podem ser ajustadas)
    private static final int PRAZO_DIAS = 7;                 // cada empréstimo dura 1 semana
    private static final int PRAZO_MAXIMO_DIAS = 28;         // 7 dias + até 3 renovações de 7 dias
    private static final BigDecimal MULTA_POR_DIA = new BigDecimal("0.50");
    private static final BigDecimal MULTA_MAXIMA = new BigDecimal("99.99"); // limite da coluna numeric(4,2)
    private static final List<StatusEmprestimo> EM_ABERTO =
            List.of(StatusEmprestimo.ATIVO, StatusEmprestimo.ATRASADO);

    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final DevolucaoRepository devolucaoRepository;
    private final MultaRepository multaRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             ExemplarRepository exemplarRepository,
                             FuncionarioRepository funcionarioRepository,
                             AlunoRepository alunoRepository,
                             ProfessorRepository professorRepository,
                             DevolucaoRepository devolucaoRepository,
                             MultaRepository multaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.exemplarRepository = exemplarRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.devolucaoRepository = devolucaoRepository;
        this.multaRepository = multaRepository;
    }

    // Empresta UM EXEMPLAR a um aluno ou a um professor
    public EmprestimoDTO realizarEmprestimo(RealizarEmprestimoDTO dto) {
        if ((dto.idAluno() == null) == (dto.idProfessor() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o aluno OU o professor (apenas um)");
        }
        if (dto.idFuncionario() == null || dto.idExemplar() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o exemplar e o funcionário");
        }

        Funcionario funcionario = funcionarioRepository.findById(dto.idFuncionario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        // A trava impede que dois empréstimos peguem o mesmo exemplar ao mesmo tempo
        Exemplar exemplar = exemplarRepository.findByIdForUpdate(dto.idExemplar())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exemplar não encontrado"));
        if (!exemplar.isDisponivel()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exemplar indisponível para empréstimo");
        }

        Emprestimo emprestimo = new Emprestimo();

        if (dto.idAluno() != null) {
            Aluno aluno = alunoRepository.findById(dto.idAluno())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
            if (multaRepository.existsByAlunoIdAndStatus(aluno.getId(), StatusMulta.PENDENTE)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Aluno bloqueado: existe multa pendente");
            }
            if (emprestimoRepository.existsByAlunoIdAndStatusIn(aluno.getId(), EM_ABERTO)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "O aluno já está com um livro emprestado");
            }
            emprestimo.setAluno(aluno);
        } else {
            Professor professor = professorRepository.findById(dto.idProfessor())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
            emprestimo.setProfessor(professor);
        }

        exemplar.setDisponivel(false);
        exemplarRepository.save(exemplar);

        LocalDate hoje = LocalDate.now();
        emprestimo.setExemplar(exemplar);
        emprestimo.setFuncionario(funcionario);
        emprestimo.setDataEmprestimo(hoje);
        emprestimo.setDataPrevistaDevolucao(hoje.plusDays(PRAZO_DIAS));
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    // Registra a devolução, libera o exemplar e gera multa se houve atraso (só para aluno)
    public EmprestimoDTO realizarDevolucao(Integer id, DevolucaoRequestDTO dto) {
        if (dto == null || dto.estadoExemplarDevolvido() == null || dto.estadoExemplarDevolvido().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o estado do exemplar devolvido");
        }
        if (dto.estadoExemplarDevolvido().length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado do exemplar deve ter até 20 caracteres");
        }

        Emprestimo emprestimo = emprestimoRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
        if (!EM_ABERTO.contains(emprestimo.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este empréstimo já foi encerrado");
        }

        LocalDate hoje = LocalDate.now();
        boolean multaGerada = false;

        if (emprestimo.getAluno() != null && hoje.isAfter(emprestimo.getDataPrevistaDevolucao())) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataPrevistaDevolucao(), hoje);
            BigDecimal valor = MULTA_POR_DIA.multiply(BigDecimal.valueOf(diasAtraso))
                    .min(MULTA_MAXIMA)
                    .setScale(2, RoundingMode.HALF_UP);

            Multa multa = new Multa();
            multa.setAluno(emprestimo.getAluno());
            multa.setValor(valor);
            multa.setStatus(StatusMulta.PENDENTE);
            multa.setDataInicio(hoje);
            multaRepository.save(multa);
            multaGerada = true;
        }

        Devolucao devolucao = new Devolucao();
        devolucao.setEmprestimo(emprestimo);
        devolucao.setDataDevolucaoEfetiva(hoje);
        devolucao.setEstadoExemplarDevolvido(dto.estadoExemplarDevolvido());
        devolucao.setObservacoes(dto.observacoes());
        devolucao.setMultaGerada(multaGerada);
        devolucaoRepository.save(devolucao);

        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.setDisponivel(true);
        exemplar.setEstadoConservacao(dto.estadoExemplarDevolvido());
        exemplarRepository.save(exemplar);

        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    // Soma 7 dias ao prazo. Como não há coluna de renovações, o limite é de 28 dias no total.
    public EmprestimoDTO renovarEmprestimo(Integer id) {
        Emprestimo emprestimo = emprestimoRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este empréstimo não pode ser renovado");
        }
        if (LocalDate.now().isAfter(emprestimo.getDataPrevistaDevolucao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Empréstimo atrasado não pode ser renovado");
        }
        long diasTotais = ChronoUnit.DAYS.between(emprestimo.getDataEmprestimo(), emprestimo.getDataPrevistaDevolucao());
        if (diasTotais >= PRAZO_MAXIMO_DIAS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Limite de renovações atingido");
        }

        emprestimo.setDataPrevistaDevolucao(emprestimo.getDataPrevistaDevolucao().plusDays(PRAZO_DIAS));
        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    public List<EmprestimoDTO> listarTodos() {
        return emprestimoRepository.findAll().stream().map(EmprestimoDTO::from).toList();
    }

    public List<EmprestimoDTO> listarPorAluno(Integer alunoId) {
        return emprestimoRepository.findByAlunoId(alunoId).stream().map(EmprestimoDTO::from).toList();
    }

    public EmprestimoDTO buscarPorId(Integer id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
        return EmprestimoDTO.from(emprestimo);
    }
}
