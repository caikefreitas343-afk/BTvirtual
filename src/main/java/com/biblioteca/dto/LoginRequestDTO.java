package com.biblioteca.dto;

public class LoginRequestDTO {
    private String usuario;
    private String senha;
    private String email;
    private String matricula;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getIdentificador() {
        if (usuario != null && !usuario.isBlank()) return usuario;
        if (email != null && !email.isBlank()) return email;
        return matricula;
    }
}
