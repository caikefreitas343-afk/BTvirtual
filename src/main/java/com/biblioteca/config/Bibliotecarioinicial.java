package com.biblioteca.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.biblioteca.entity.TipoUsuario;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.UsuarioRepository;

@Component
public class Bibliotecarioinicial implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${biblioteca.bibliotecario.nome:Bibliotecário}")
    private String nome;

    @Value("${biblioteca.bibliotecario.matricula:}")
    private String matricula;

    @Value("${biblioteca.bibliotecario.email:}")
    private String email;

    @Value("${biblioteca.bibliotecario.senha:}")
    private String senha;

    public Bibliotecarioinicial(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (email.isBlank() || matricula.isBlank() || senha.isBlank()) {
            return; // NÃO configurado, não cria nada
        }
        if (usuarioRepository.findByEmail(email)!= null 
        || usuarioRepository.findByMatricula(matricula)!= null) {
            return;
        }

        Usuario bibliotecario = new Usuario(
                null,
                nome,
                matricula,
                email,
                passwordEncoder.encode(senha),
                TipoUsuario.BIBLIOTECARIO
        );
        usuarioRepository.save(bibliotecario);
    }
}
