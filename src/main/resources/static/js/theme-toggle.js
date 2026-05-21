(function () {
    const storageKey = "mgcss-theme";
    const root = document.documentElement;
    const button = document.querySelector("[data-theme-toggle]");
    const label = document.querySelector("[data-theme-label]");

    function setTheme(theme) {
        root.dataset.theme = theme;
        localStorage.setItem(storageKey, theme);
        if (label) {
            label.textContent = theme === "day" ? "Dia" : "Noche";
        }
    }

    const savedTheme = localStorage.getItem(storageKey);
    setTheme(savedTheme === "day" ? "day" : "night");

    if (button) {
        button.addEventListener("click", function () {
            setTheme(root.dataset.theme === "day" ? "night" : "day");
        });
    }
}());
