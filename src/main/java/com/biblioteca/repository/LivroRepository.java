package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    // Procura pelo nome (ou nome artístico) do autor
    @Query("select distinct l from Livro l join l.autores a "
            + "where lower(a.prenomeAutor) like lower(concat('%', :termo, '%')) "
            + "or lower(a.sobrenomeAutor) like lower(concat('%', :termo, '%')) "
            + "or lower(a.nomeArtistico) like lower(concat('%', :termo, '%'))")
    List<Livro> buscarPorAutor(@Param("termo") String termo);
}
