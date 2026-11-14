package com.biblioteca.service;

import com.biblioteca.dto.TurmaDTO;
import com.biblioteca.entity.Turma;
import com.biblioteca.repository.TurmaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public TurmaDTO criar(TurmaDTO dto) {
        Turma turma = new Turma();
        turma.setCodigoTurma(dto.codigoTurma());
        turma.setSerie(dto.serie());
        turma.setTurno(dto.turno());
        turma.setAnoLetivo(dto.anoLetivo());
        return TurmaDTO.from(turmaRepository.save(turma));
    }

    public List<TurmaDTO> listarTodas() {
        return turmaRepository.findAll().stream().map(TurmaDTO::from).toList();
    }

    public TurmaDTO buscarPorId(Integer id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada"));
        return TurmaDTO.from(turma);
    }
}
