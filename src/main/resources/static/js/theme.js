(function () {
  var root = document.documentElement;

  // Runs immediately (while the page is loading) so there's no white flash
  var saved = null;
  try { saved = localStorage.getItem('theme'); } catch (e) {}
  var prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
  root.setAttribute('data-bs-theme', saved || (prefersDark ? 'dark' : 'light'));

  function setTheme(theme, btn) {
    root.setAttribute('data-bs-theme', theme);
    try { localStorage.setItem('theme', theme); } catch (e) {}
    var icon = btn && btn.querySelector('i');
    if (icon) icon.className = theme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-stars-fill';
  }

  // Wires up the button once the page has loaded
  document.addEventListener('DOMContentLoaded', function () {
    var btn = document.getElementById('themeToggle');
    if (!btn) return; // e.g. login page has no navbar
    setTheme(root.getAttribute('data-bs-theme'), btn);
    btn.addEventListener('click', function () {
      setTheme(root.getAttribute('data-bs-theme') === 'dark' ? 'light' : 'dark', btn);
    });
  });
})();