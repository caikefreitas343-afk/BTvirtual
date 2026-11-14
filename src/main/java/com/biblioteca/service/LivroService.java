package com.biblioteca.service;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Categoria;
import com.biblioteca.entity.Editora;
import com.biblioteca.entity.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.CategoriaRepository;
import com.biblioteca.repository.EditoraRepository;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class LivroService {

    private final LivroRepository livroRepository;
    private final EditoraRepository editoraRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ExemplarRepository exemplarRepository;

    public LivroService(LivroRepository livroRepository,
                        EditoraRepository editoraRepository,
                        AutorRepository autorRepository,
                        CategoriaRepository categoriaRepository,
                        ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.editoraRepository = editoraRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.exemplarRepository = exemplarRepository;
    }

    // CREATE
    public LivroDTO criar(LivroRequestDTO dto) {
        Livro livro = new Livro();
        preencher(livro, dto);
        return paraDTO(livroRepository.save(livro));
    }

    // READ
    public List<LivroDTO> listarTodos() {
        return livroRepository.findAll().stream().map(this::paraDTO).toList();
    }

    public LivroDTO buscarPorId(Integer id) {
        return paraDTO(buscarEntidade(id));
    }

    public List<LivroDTO> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo).stream().map(this::paraDTO).toList();
    }

    public List<LivroDTO> buscarPorAutor(String autor) {
        return livroRepository.buscarPorAutor(autor).stream().map(this::paraDTO).toList();
    }

    // UPDATE
    public LivroDTO atualizar(Integer id, LivroRequestDTO dto) {
        Livro livro = buscarEntidade(id);
        preencher(livro, dto);
        return paraDTO(livroRepository.save(livro));
    }

    // DELETE
    public void deletar(Integer id) {
        Livro livro = buscarEntidade(id);
        if (exemplarRepository.countByLivroId(id) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir um livro que ainda tem exemplares cadastrados");
        }
        livroRepository.delete(livro);
    }

    private void preencher(Livro livro, LivroRequestDTO dto) {
        if (dto.idEditora() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escolha a editora do livro");
        }
        Editora editora = editoraRepository.findById(dto.idEditora())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada"));

        livro.setTitulo(dto.titulo());
        livro.setEdicao(dto.edicao());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setEditora(editora);

        List<Autor> autores = dto.idsAutores() == null ? List.of() : autorRepository.findAllById(dto.idsAutores());
        livro.setAutores(new HashSet<>(autores));

        List<Categoria> categorias = dto.idsCategorias() == null ? List.of() : categoriaRepository.findAllById(dto.idsCategorias());
        livro.setCategorias(new HashSet<>(categorias));
    }

    private Livro buscarEntidade(Integer id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Livro não encontrado com o ID: " + id));
    }

    // Quantos exemplares existem e quantos estão disponíveis é contado direto nos exemplares
    private LivroDTO paraDTO(Livro livro) {
        long total = exemplarRepository.countByLivroId(livro.getId());
        long disponiveis = exemplarRepository.countByLivroIdAndDisponivelTrue(livro.getId());
        return LivroDTO.from(livro, total, disponiveis);
    }
}
