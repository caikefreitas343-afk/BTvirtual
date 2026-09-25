package com.biblioteca.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_bibliotecario")
public class Bibliotecario extends Usuario {
    
	private String cargo;
    private String setor;

    protected Bibliotecario() {
        super();
        setTipoUsuario(TipoUsuario.BIBLIOTECARIO);
    }

    // Construtor vazio (Obrigatorio para o JPA)

    public Bibliotecario(UUID id, String nome, String matricula, String email, String senha, String cargo, String setor,TipoUsuario tipoUsuario) {
        super(id, nome, matricula, email, senha, tipoUsuario);
        this.cargo = cargo;
        this.setor = setor;
    }

    // Getters e Setters
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}

