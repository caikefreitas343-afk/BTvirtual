let tab = "andamento";

function renderReadings() {
  const done = tab === "concluidos";
  const list = books.filter((b) => b.progress && (done ? b.progress === 100 : b.progress < 100));
  const box = $("#readList");
  if (!list.length) {
    box.innerHTML = `<div class="empty-box"><h3>${done ? "Nenhuma leitura concluída ainda" : "Você não tem leituras em andamento"}</h3><p>Escolha um livro no acervo para começar.</p><button class="btn-dark" data-href="TelaInicial.html">Explorar o acervo</button></div>`;
    return;
  }
  box.innerHTML = list.map((b) => `
    <article class="reading">
      <div class="mini-cover cover-mini-${b.cover}"><small>${b.author.toUpperCase()}</small><strong>${b.title.toUpperCase()}</strong></div>
      <div class="reading-info">
        <small>${b.label}</small>
        <h3>${b.title}</h3>
        <p>${b.author}</p>
        <div class="progress"><span style="width:${b.progress}%"></span></div>
        <div class="progress-meta"><span>${b.chapter}</span><span>${b.progress}% lido</span></div>
      </div>
      ${done ? `<span class="done-tag">Concluído</span>` : `<button class="btn-dark">Continuar leitura ${arrowIcon}</button>`}
    </article>`).join("");
}

$("#readTabs").addEventListener("click", (e) => {
  const t = e.target.closest(".chip"); if (!t) return;
  $$("#readTabs .chip").forEach((c) => c.classList.remove("active"));
  t.classList.add("active");
  tab = t.dataset.tab;
  renderReadings();
});
renderReadings();
