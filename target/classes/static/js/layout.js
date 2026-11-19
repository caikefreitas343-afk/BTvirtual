// Comportamentos comuns a todas as telas: menu, botões de link e saída da conta.

// Botões que levam a outra tela (data-href)
document.addEventListener("click", (e) => {
  const el = e.target.closest("[data-href]");
  if (el) window.location.href = el.dataset.href;
});

// Texto maior (configuração salva)
document.body.classList.toggle("big-text", !!settings.textoMaior);

// Sair da conta -> confirmação -> Login.html
const modal = $("#logoutModal");
$("#logoutLink").addEventListener("click", (e) => { e.preventDefault(); modal.hidden = false; $("#logoutConfirm").focus(); });
$("#logoutCancel").addEventListener("click", () => (modal.hidden = true));
modal.addEventListener("click", (e) => { if (e.target === modal) modal.hidden = true; });
document.addEventListener("keydown", (e) => { if (e.key === "Escape") modal.hidden = true; });
$("#logoutConfirm").addEventListener("click", () => {
  // Quando existir autenticação no Spring, chame aqui o logout (ex.: fetch("/logout", { method: "POST" })).
  window.location.href = "Login.html";
});
