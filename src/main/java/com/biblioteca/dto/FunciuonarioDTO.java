package com.biblioteca.dto;

    import com.biblioteca.entity.Funcionario;

    public record FunciuonarioDTO(
            String nome,
            String matricula,
            String email,
            String cargo,
            String setor
    ) {
        public FunciuonarioDTO(String nome, String matricula, String email, String cargo, String setor) {
            this.nome = nome;
            this.matricula = matricula;
            this.email = email;
            this.cargo = cargo;
            this.setor = setor;
        }

        public FunciuonarioDTO(Funcionario funcionario) {
            this(
                    funcionario.getNome(),
                    funcionario.getMatricula(),
                    funcionario.getEmail(),
                    funcionario.getCargo(),
                    funcionario.getSetor()
            );
        }
    }
    

