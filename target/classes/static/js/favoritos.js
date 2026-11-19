function renderFavorites() {
  const list = books.filter((b) => favorites.has(b.id));
  $("#favGrid").innerHTML = list.map(cardTemplate).join("");
  $("#favEmpty").hidden = list.length > 0;
}
$("#favGrid").addEventListener("click", (e) => { if (toggleFavorite(e)) renderFavorites(); });
renderFavorites();
