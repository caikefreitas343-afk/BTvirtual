package com.biblioteca.service;

import com.biblioteca.dto.ExemplarDTO;
import com.biblioteca.dto.ExemplarRequestDTO;
import com.biblioteca.entity.Exemplar;
import com.biblioteca.entity.Livro;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;
    private final LivroRepository livroRepository;

    public ExemplarService(ExemplarRepository exemplarRepository, LivroRepository livroRepository) {
        this.exemplarRepository = exemplarRepository;
        this.livroRepository = livroRepository;
    }

    // CREATE
    public ExemplarDTO criar(ExemplarRequestDTO dto) {
        if (exemplarRepository.existsByNumeroTombo(dto.numeroTombo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um exemplar com esse número de tombo");
        }
        Exemplar exemplar = new Exemplar();
        exemplar.setLivro(buscarLivro(dto.idLivro()));
        exemplar.setNumeroTombo(dto.numeroTombo());
        exemplar.setEstadoConservacao(dto.estadoConservacao());
        exemplar.setDataAquisicao(dto.dataAquisicao() != null ? dto.dataAquisicao() : LocalDate.now());
        exemplar.setDisponivel(dto.disponivel() == null || dto.disponivel());
        return ExemplarDTO.from(exemplarRepository.save(exemplar));
    }

    // READ
    public List<ExemplarDTO> listarTodos() {
        return exemplarRepository.findAll().stream().map(ExemplarDTO::from).toList();
    }

    public ExemplarDTO buscarPorId(Integer id) {
        return ExemplarDTO.from(buscarEntidade(id));
    }

    public ExemplarDTO buscarPorNumeroTombo(String numeroTombo) {
        Exemplar exemplar = exemplarRepository.findByNumeroTombo(numeroTombo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Exemplar não encontrado com o tombo: " + numeroTombo));
        return ExemplarDTO.from(exemplar);
    }

    public List<ExemplarDTO> listarPorLivro(Integer livroId) {
        return exemplarRepository.findByLivroId(livroId).stream().map(ExemplarDTO::from).toList();
    }

    // UPDATE
    public ExemplarDTO atualizar(Integer id, ExemplarRequestDTO dto) {
        Exemplar exemplar = buscarEntidade(id);

        if (!exemplar.getNumeroTombo().equals(dto.numeroTombo())
                && exemplarRepository.existsByNumeroTombo(dto.numeroTombo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um exemplar com esse número de tombo");
        }

        if (dto.idLivro() != null) {
            exemplar.setLivro(buscarLivro(dto.idLivro()));
        }
        exemplar.setNumeroTombo(dto.numeroTombo());
        exemplar.setEstadoConservacao(dto.estadoConservacao());
        if (dto.dataAquisicao() != null) {
            exemplar.setDataAquisicao(dto.dataAquisicao());
        }
        if (dto.disponivel() != null) {
            exemplar.setDisponivel(dto.disponivel());
        }
        return ExemplarDTO.from(exemplarRepository.save(exemplar));
    }

    // DELETE
    public void deletar(Integer id) {
        Exemplar exemplar = buscarEntidade(id);
        if (!exemplar.isDisponivel()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir um exemplar que está emprestado ou indisponível");
        }
        exemplarRepository.delete(exemplar);
    }

    private Exemplar buscarEntidade(Integer id) {
        return exemplarRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Exemplar não encontrado com o ID: " + id));
    }

    private Livro buscarLivro(Integer idLivro) {
        if (idLivro == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o livro do exemplar");
        }
        return livroRepository.findById(idLivro)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Livro não encontrado com o ID: " + idLivro));
    }
}
