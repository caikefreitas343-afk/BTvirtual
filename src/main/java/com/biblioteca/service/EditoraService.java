package com.biblioteca.service;

import com.biblioteca.dto.EditoraDTO;
import com.biblioteca.entity.Editora;
import com.biblioteca.repository.EditoraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EditoraService {

    private final EditoraRepository editoraRepository;

    public EditoraService(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    public EditoraDTO criar(EditoraDTO dto) {
        Editora editora = new Editora();
        editora.setNome(dto.nome());
        editora.setCnpj(dto.cnpj());
        return EditoraDTO.from(editoraRepository.save(editora));
    }

    public List<EditoraDTO> listarTodas() {
        return editoraRepository.findAll().stream().map(EditoraDTO::from).toList();
    }
}
