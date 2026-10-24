document.addEventListener('DOMContentLoaded', function () {
    var cuerpo = document.body;

    document.querySelectorAll('[data-abrir-menu]').forEach(function (boton) {
        boton.addEventListener('click', function () {
            cuerpo.classList.add('menu-abierto');
        });
    });

    document.querySelectorAll('[data-cerrar-menu]').forEach(function (elemento) {
        elemento.addEventListener('click', function () {
            cuerpo.classList.remove('menu-abierto');
        });
    });

    document.querySelectorAll('input[data-filtrar]').forEach(function (campo) {
        campo.addEventListener('input', function () {
            var texto = campo.value.trim().toLowerCase();
            document.querySelectorAll(campo.dataset.filtrar).forEach(function (elemento) {
                elemento.hidden = texto !== '' && !elemento.textContent.toLowerCase().includes(texto);
            });
        });
    });

    var usuario = document.getElementById('usuario');
    document.querySelectorAll('input[name="rol"][data-ejemplo]').forEach(function (opcion) {
        function actualizar() {
            if (opcion.checked && usuario) {
                usuario.placeholder = opcion.dataset.ejemplo;
            }
        }
        opcion.addEventListener('change', actualizar);
        actualizar();
    });
});
