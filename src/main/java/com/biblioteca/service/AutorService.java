package com.biblioteca.service;

import com.biblioteca.dto.AutorDTO;
import com.biblioteca.entity.Autor;
import com.biblioteca.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public AutorDTO criar(AutorDTO dto) {
        Autor autor = new Autor();
        autor.setNomeArtistico(dto.nomeArtistico());
        autor.setPrenomeAutor(dto.prenomeAutor());
        autor.setSobrenomeAutor(dto.sobrenomeAutor());
        return AutorDTO.from(autorRepository.save(autor));
    }

    public List<AutorDTO> listarTodos() {
        return autorRepository.findAll().stream().map(AutorDTO::from).toList();
    }
}
