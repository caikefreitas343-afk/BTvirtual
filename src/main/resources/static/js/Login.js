//src/main/resources/templates/js/Login.js

const formLogin = document.getElementById('loginForm');
const cadastroForm = document.getElementById('cadastroForm');
const mensagem = document.getElementById('mensagem') || document.createElement('p');

if (!mensagem.id) {
    mensagem.id = 'mensagem';
    mensagem.setAttribute('role', 'status');
    (formLogin || cadastroForm)?.appendChild(mensagem);
}

function mostrarMensagem(texto, sucesso = false) {
    mensagem.textContent = texto;
    mensagem.style.color = sucesso ? '#2e7d32' : '#c0392b';
}

// LOGIN
if (formLogin) {
    formLogin.addEventListener('submit', async (e) => {
        e.preventDefault();
        mostrarMensagem('');

        try {
            const resposta = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    login: formLogin.elements.namedItem('login').value,
                    senha: formLogin.elements.namedItem('senha').value
                })
            });

            if (!resposta.ok) {
                mostrarMensagem('Usuário ou senha inválidos.');
                return;
            }

            const usuario = await resposta.json();
            sessionStorage.setItem('usuario', JSON.stringify(usuario));

            // Segunda tela (ajustar os nomes quando criar as telas)
            const destinos = {
                'BIBLIOTECARIO': '/bibliotecario.html',
                'ALUNO': '/aluno.html'
            };
            const destino = destinos[usuario.tipoUsuario]

            // PROFESSOR e GESTAO ainda não possuem telas proprias

            if (!destino) {
                sessionStorage.removeItem('usuario');
                mostrarMensagem('Tipo de usuário não tem tela disponivel.');
                return;
            }

            window.location.href = destinos[usuario.tipoUsuario] || '/aluno.html';
        } catch (erro) {
            mostrarMensagem('Não foi possível conectar ao servidor. Tente novamente mais tarde.');
        }
    });
}

// CADASTRO (aluno)
if (cadastroForm) {
    cadastroForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        mostrarMensagem('');

        if (document.getElementById('senha').value.length < 6) {
            mostrarMensagem('A senha deve ter no mínimo 6 caracteres.');
            return;
        }

        try {
            const resposta = await fetch('/auth/cadastro', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    nome: document.getElementById('nome').value,
                    matricula: document.getElementById('matricula').value,
                    email: document.getElementById('email').value,
                    senha: document.getElementById('senha').value,
                    turma: document.getElementById('turma').value,
                    serie: document.getElementById('serie').value
                })
            });

            if (resposta.status === 409) {
                mostrarMensagem('E-mail ou matrícula já cadastrados.');
                return;
            }

            if (!resposta.ok) {
                mostrarMensagem('Não foi possível realizar o cadastro.');
                return;
            }

            mostrarMensagem('Cadastro realizado com sucesso!', true);
            cadastroForm.reset();
            setTimeout(() => {
                window.location.href = '/Login.html';
            }, 1500);
        } catch (erro) {
            mostrarMensagem('Não foi possível conectar ao servidor. Tente novamente mais tarde.');
        }
    });
}
