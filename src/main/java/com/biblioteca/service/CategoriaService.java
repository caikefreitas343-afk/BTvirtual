package com.biblioteca.service;

import com.biblioteca.dto.CategoriaDTO;
import com.biblioteca.entity.Categoria;
import com.biblioteca.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaDTO criar(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.nomeCategoria());
        return CategoriaDTO.from(categoriaRepository.save(categoria));
    }

    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream().map(CategoriaDTO::from).toList();
    }
}
