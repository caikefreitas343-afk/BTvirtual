package com.biblioteca.dto;

    import com.biblioteca.entity.Bibliotecario;

    public record BibliotecarioDTO(
            String nome,
            String matricula,
            String email,
            String cargo,
            String setor
    ) {
        public BibliotecarioDTO(String nome, String matricula, String email, String cargo, String setor) {
            this.nome = nome;
            this.matricula = matricula;
            this.email = email;
            this.cargo = cargo;
            this.setor = setor;
        }

        public BibliotecarioDTO(Bibliotecario bibliotecario) {
            this(
                    bibliotecario.getNome(),
                    bibliotecario.getMatricula(),
                    bibliotecario.getEmail(),
                    bibliotecario.getCargo(),
                    bibliotecario.getSetor()
            );
        }
    }
    

