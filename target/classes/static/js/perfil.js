function renderProfile() {
  const p = store.get("profile", { name: "Ana Souza", cls: "9º ano B", mail: "ana.souza@colegiohorizonte.edu.br" });
  $("#pName").textContent = p.name;
  $("#pClass").textContent = `${p.cls} • Colégio Horizonte`;
  $("#fName").value = p.name; $("#fClass").value = p.cls; $("#fMail").value = p.mail;
  $("#stReading").textContent = books.filter((b) => b.progress && b.progress < 100).length;
  $("#stDone").textContent = books.filter((b) => b.progress === 100).length;
  $("#stFav").textContent = favorites.size;
}

$("#profileForm").addEventListener("submit", (e) => {
  e.preventDefault();
  store.set("profile", { name: $("#fName").value.trim(), cls: $("#fClass").value.trim(), mail: $("#fMail").value.trim() });
  renderProfile();
  const msg = $("#savedMsg"); msg.hidden = false; setTimeout(() => (msg.hidden = true), 2500);
});
renderProfile();
