package com.biblioteca.service;

import com.biblioteca.dto.MultaDTO;
import com.biblioteca.dto.MultaRequestDTO;
import com.biblioteca.entity.Aluno;
import com.biblioteca.entity.Multa;
import com.biblioteca.entity.StatusMulta;
import com.biblioteca.repository.AlunoRepository;
import com.biblioteca.repository.MultaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MultaService {

    private static final BigDecimal VALOR_MAXIMO = new BigDecimal("99.99"); // limite da coluna numeric(4,2)

    private final MultaRepository multaRepository;
    private final AlunoRepository alunoRepository;

    public MultaService(MultaRepository multaRepository, AlunoRepository alunoRepository) {
        this.multaRepository = multaRepository;
        this.alunoRepository = alunoRepository;
    }

    // Multa aplicada pelo bibliotecário (ex.: livro perdido)
    public MultaDTO criar(MultaRequestDTO dto) {
        if (dto.idAluno() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o aluno");
        }
        if (dto.valor() == null || dto.valor().signum() <= 0 || dto.valor().compareTo(VALOR_MAXIMO) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor da multa deve ficar entre 0,01 e 99,99");
        }
        Aluno aluno = alunoRepository.findById(dto.idAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));

        Multa multa = new Multa();
        multa.setAluno(aluno);
        multa.setValor(dto.valor().setScale(2, RoundingMode.HALF_UP));
        multa.setStatus(StatusMulta.PENDENTE);
        multa.setDataInicio(LocalDate.now());
        return MultaDTO.from(multaRepository.save(multa));
    }

    public List<MultaDTO> listarTodas() {
        return multaRepository.findAll().stream().map(MultaDTO::from).toList();
    }

    public List<MultaDTO> listarPorAluno(Integer alunoId) {
        return multaRepository.findByAlunoId(alunoId).stream().map(MultaDTO::from).toList();
    }

    // Registra o pagamento: a multa deixa de bloquear o aluno
    public MultaDTO pagar(Integer id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Multa não encontrada"));
        if (multa.getStatus() != StatusMulta.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta multa não está pendente");
        }
        multa.setStatus(StatusMulta.PAGA);
        multa.setDataFim(LocalDate.now());
        return MultaDTO.from(multaRepository.save(multa));
    }
}
