$$(".switch").forEach((sw) => {
  sw.checked = !!settings[sw.dataset.setting];
  sw.addEventListener("change", () => {
    settings[sw.dataset.setting] = sw.checked;
    store.set("settings", settings);
    document.body.classList.toggle("big-text", !!settings.textoMaior);
  });
});
