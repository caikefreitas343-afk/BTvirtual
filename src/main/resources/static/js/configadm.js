
const API = "http://localhost:8080/api/alunos";
const CAMPO_ID = "id";                          

// Campos do AlunoDTO. A 1ª linha é o título de cada item da lista.
// editavel: false = aparece no formulário, mas não é enviado no PUT.
const CAMPOS = [
  { chave: "nome",      rotulo: "Nome",      tipo: "text",  editavel: true,  obrigatorio: true },
  { chave: "matricula", rotulo: "Matrícula", tipo: "text",  editavel: false },
  { chave: "email",     rotulo: "E-mail",    tipo: "email", editavel: true },
];

function cabecalhos() {
  const h = { "Content-Type": "application/json" };
  // Se a API exigir login (JWT), descomente e use a chave onde a tela de login guardou o token:
  // h.Authorization = "Bearer " + localStorage.getItem("token");
  return h;
}
// ==============================================

const lista = document.getElementById("lista");
const vazio = document.getElementById("vazio");
const carregando = document.getElementById("carregando");
const erro = document.getElementById("erro");
const dlgEditar = document.getElementById("dlg-editar");
const dlgExcluir = document.getElementById("dlg-excluir");
const formEditar = document.getElementById("form-editar");
const camposEditar = document.getElementById("campos-editar");

let alunos = [];
let idAtual = null;

const mesmoId = (a, id) => String(a[CAMPO_ID]) === String(id);

function mostrarErro(msg) {
  erro.textContent = msg;
  erro.hidden = !msg;
}

async function requisicao(url, opcoes) {
  const resp = await fetch(url, { headers: cabecalhos(), ...opcoes });
  if (!resp.ok) throw new Error(`Erro ${resp.status} ao falar com o servidor.`);
  return resp.status === 204 ? null : resp.json().catch(() => null);
}

// Monta os inputs do formulário de edição a partir de CAMPOS
CAMPOS.forEach((c) => {
  const label = document.createElement("label");
  label.textContent = c.rotulo;
  const input = document.createElement("input");
  input.name = c.chave;
  input.type = c.tipo;
  input.required = !!c.obrigatorio;
  input.readOnly = c.editavel === false;
  label.appendChild(input);
  camposEditar.appendChild(label);
});

// LISTAR (GET /api/alunos)
async function carregar() {
  mostrarErro("");
  carregando.hidden = false;
  try {
    alunos = (await requisicao(API)) || [];
    renderizar();
  } catch (e) {
    mostrarErro("Não foi possível carregar os alunos. " + e.message);
  } finally {
    carregando.hidden = true;
  }
}

function renderizar() {
  lista.innerHTML = "";
  vazio.hidden = alunos.length > 0;

  alunos.forEach((a) => {
    const li = document.createElement("li");
    li.className = "cfg-item";

    const dados = document.createElement("div");
    dados.className = "cfg-dados";
    CAMPOS.forEach((c, i) => {
      const el = document.createElement(i === 0 ? "strong" : "span");
      const valor = a[c.chave] ?? "";
      el.textContent = i === 0 ? valor || "(sem nome)" : `${c.rotulo}: ${valor}`;
      dados.appendChild(el);
    });

    const botoes = document.createElement("div");
    botoes.className = "cfg-botoes";
    const bEditar = document.createElement("button");
    bEditar.className = "btn";
    bEditar.textContent = "Editar";
    bEditar.addEventListener("click", () => abrirEdicao(a[CAMPO_ID]));
    const bExcluir = document.createElement("button");
    bExcluir.className = "btn btn-perigo";
    bExcluir.textContent = "Excluir";
    bExcluir.addEventListener("click", () => abrirExclusao(a[CAMPO_ID]));
    botoes.append(bEditar, bExcluir);

    li.append(dados, botoes);
    lista.appendChild(li);
  });
}

// EDITAR (PUT /api/alunos/{id})
function abrirEdicao(id) {
  const a = alunos.find((x) => mesmoId(x, id));
  if (!a) return;
  idAtual = id;
  CAMPOS.forEach((c) => { formEditar.elements[c.chave].value = a[c.chave] ?? ""; });
  dlgEditar.showModal();
}

formEditar.addEventListener("submit", async () => {
  const corpo = {};
  CAMPOS.filter((c) => c.editavel !== false).forEach((c) => {
    corpo[c.chave] = formEditar.elements[c.chave].value.trim();
  });
  try {
    await requisicao(`${API}/${idAtual}`, { method: "PUT", body: JSON.stringify(corpo) });
    mostrarErro("");
    await carregar();
  } catch (e) {
    mostrarErro("Não foi possível salvar a edição. " + e.message);
  }
});

// EXCLUIR (DELETE /api/alunos/{id})
function abrirExclusao(id) {
  const a = alunos.find((x) => mesmoId(x, id));
  if (!a) return;
  idAtual = id;
  document.getElementById("txt-excluir").textContent =
    `${a[CAMPOS[0].chave] || "Este aluno"} será removido e não dá para desfazer.`;
  dlgExcluir.showModal();
}

document.getElementById("confirmar-excluir").addEventListener("click", async () => {
  try {
    await requisicao(`${API}/${idAtual}`, { method: "DELETE" });
    mostrarErro("");
    dlgExcluir.close();
    await carregar();
  } catch (e) {
    dlgExcluir.close();
    mostrarErro("Não foi possível excluir. " + e.message);
  }
});

document.querySelectorAll("[data-fechar]").forEach((b) =>
  b.addEventListener("click", () => b.closest("dialog").close())
);

carregar();