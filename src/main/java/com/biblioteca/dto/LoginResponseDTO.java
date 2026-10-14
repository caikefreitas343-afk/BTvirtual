package com.biblioteca.dto;

public class LoginResponseDTO {
    private String nome;
    private String email;
    private String tipoUsuario;
    private String mensagem;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String nome, String email, String tipoUsuario, String mensagem) {
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
