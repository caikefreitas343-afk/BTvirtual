package com.biblioteca.config;

import com.biblioteca.entity.Funcionario;
import com.biblioteca.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// Cria o primeiro bibliotecário ao iniciar o projeto, se ele ainda não existir.
// Os dados vêm do application.properties (chaves biblioteca.bibliotecario.*).
@Component
public class Bibliotecarioinicial implements CommandLineRunner {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${biblioteca.bibliotecario.nome:Bibliotecário}")
    private String nome;

    @Value("${biblioteca.bibliotecario.matricula:}")
    private String matricula;

    @Value("${biblioteca.bibliotecario.email:}")
    private String email;

    @Value("${biblioteca.bibliotecario.senha:}")
    private String senha;

    public Bibliotecarioinicial(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (email.isBlank() || matricula.isBlank() || senha.isBlank()) {
            return; // não configurado: não cria nada
        }
        if (funcionarioRepository.existsByEmail(email) || funcionarioRepository.existsByMatriculaFuncional(matricula)) {
            return; // já existe
        }

        Funcionario bibliotecario = new Funcionario();
        bibliotecario.setNome(nome);
        bibliotecario.setEmail(email);
        bibliotecario.setMatriculaFuncional(matricula);
        bibliotecario.setCargo("Bibliotecário");
        bibliotecario.setDataAdmissao(LocalDate.now());
        bibliotecario.setSenha(passwordEncoder.encode(senha));
        funcionarioRepository.save(bibliotecario);
    }
}
