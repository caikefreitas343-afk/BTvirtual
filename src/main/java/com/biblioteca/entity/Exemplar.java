package com.biblioteca.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exemplar")
public class Exemplar extends Livro {
    @Id
    private Long id;

    // Getters e setters 


}
