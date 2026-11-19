// ========== Dados (troque depois por chamadas à API do Spring Boot) ==========
const books = [
  { id: 1, title: "Dom Casmurro", author: "Machado de Assis", category: "Literatura", label: "LITERATURA BRASILEIRA", cover: "dom", available: true, popularity: 98, progress: 8, chapter: "Capítulo 12 de 148" },
  { id: 2, title: "O Pequeno Príncipe", author: "Antoine de Saint-Exupéry", category: "Infantojuvenil", label: "INFANTOJUVENIL", cover: "principe", available: true, popularity: 95, progress: 64, chapter: "Capítulo 16 de 27" },
  { id: 3, title: "Vidas Secas", author: "Graciliano Ramos", category: "Literatura", label: "LITERATURA BRASILEIRA", cover: "vidas", available: false, popularity: 90 },
  { id: 4, title: "A Hora da Estrela", author: "Clarice Lispector", category: "Literatura", label: "LITERATURA BRASILEIRA", cover: "hora", available: true, popularity: 88, progress: 100, chapter: "Leitura finalizada" },
  { id: 5, title: "O Cortiço", author: "Aluísio Azevedo", category: "Literatura", label: "LITERATURA BRASILEIRA", cover: "cortico", available: true, popularity: 85 },
];

const covers = {
  dom: `<small>MACHADO DE ASSIS</small><div class="title">DOM<br>CASMURRO</div><div class="lines"><i></i><i></i><i></i><i></i></div>`,
  principe: `<small>ANTOINE DE<br>SAINT-EXUPÉRY</small><div class="title">O<br>pequeno<br>príncipe</div><span class="moon"></span>`,
  vidas: `<small>GRACILIANO RAMOS</small><div class="title">VIDAS<br>SECAS</div><div class="lines"><i></i><i></i><i></i><i></i></div>`,
  hora: `<small>CLARICE LISPECTOR</small><div class="title">A HORA<br>DA<br>ESTRELA</div><span class="star"></span>`,
  cortico: `<small>ALUÍSIO AZEVEDO</small><div class="title">O<br>CORTIÇO</div><div class="bars"><i></i><i></i><i></i><i></i><i></i></div>`,
};

const bookmarkIcon = `<svg viewBox="0 0 24 24"><path d="M7 4h10v16l-5-3.5L7 20V4Z"/></svg>`;
const arrowIcon = `<svg viewBox="0 0 24 24"><path d="M5 12h14M13 6l6 6-6 6"/></svg>`;

// ========== Utilidades ==========
const $ = (s) => document.querySelector(s);
const $$ = (s) => document.querySelectorAll(s);
const normalize = (s) => s.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();

const store = {
  get(key, fallback) { try { return JSON.parse(localStorage.getItem("bib:" + key)) ?? fallback; } catch { return fallback; } },
  set(key, value) { try { localStorage.setItem("bib:" + key, JSON.stringify(value)); } catch {} },
};

// ========== Favoritos (compartilhado entre as telas) ==========
const favorites = new Set(store.get("favorites", [4]));

function toggleFavorite(e) {
  const btn = e.target.closest(".fav");
  if (!btn) return false;
  const id = Number(btn.dataset.id);
  favorites.has(id) ? favorites.delete(id) : favorites.add(id);
  store.set("favorites", [...favorites]);
  return true;
}

function cardTemplate(b) {
  const fav = favorites.has(b.id);
  return `
    <article class="card">
      <div class="cover-area cover-bg-${b.cover}"><div class="book ${b.cover}">${covers[b.cover]}</div></div>
      <div class="card-body">
        <small>${b.label}</small>
        <h3>${b.title}</h3>
        <p class="author">${b.author}</p>
        <div class="card-foot">
          <span class="status ${b.available ? "ok" : "out"}">${b.available ? "Disponível" : "Emprestado"}</span>
          <button class="fav ${fav ? "on" : ""}" data-id="${b.id}" aria-label="${fav ? "Remover dos favoritos" : "Salvar nos favoritos"}" aria-pressed="${fav}">${bookmarkIcon}</button>
        </div>
      </div>
    </article>`;
}

// ========== Configurações (compartilhado) ==========
const settings = store.get("settings", { devolucao: true, disponivel: true, novidades: false, textoMaior: false });
