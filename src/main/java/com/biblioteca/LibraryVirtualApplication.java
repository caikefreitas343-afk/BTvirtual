package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryVirtualApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryVirtualApplication.class, args);
        System.out.println("🚀 Backend da Biblioteca Virtual rodando com sucesso!");
    }
}