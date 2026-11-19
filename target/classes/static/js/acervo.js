const view = { filter: "todos", query: "", sort: "popular" };

function renderCatalog() {
  const q = normalize(view.query.trim());
  const list = books
    .filter((b) => (view.filter === "todos" || b.category === view.filter) &&
      (!q || normalize(`${b.title} ${b.author} ${b.category}`).includes(q)))
    .sort((a, b) => view.sort === "titulo" ? a.title.localeCompare(b.title, "pt-BR")
      : view.sort === "autor" ? a.author.localeCompare(b.author, "pt-BR")
      : b.popularity - a.popularity);
  $("#grid").innerHTML = list.map(cardTemplate).join("");
  $("#empty").hidden = list.length > 0;
}

$("#chips").addEventListener("click", (e) => {
  const chip = e.target.closest(".chip"); if (!chip) return;
  $$("#chips .chip").forEach((c) => c.classList.remove("active"));
  chip.classList.add("active");
  view.filter = chip.dataset.filter;
  renderCatalog();
});
$("#sort").addEventListener("change", (e) => { view.sort = e.target.value; renderCatalog(); });
$("#searchForm").addEventListener("submit", (e) => {
  e.preventDefault();
  view.query = $("#searchInput").value;
  renderCatalog();
  $(".catalog").scrollIntoView({ behavior: "smooth", block: "start" });
});
$("#searchInput").addEventListener("input", (e) => { if (!e.target.value) { view.query = ""; renderCatalog(); } });
$("#grid").addEventListener("click", (e) => { if (toggleFavorite(e)) renderCatalog(); });

renderCatalog();
