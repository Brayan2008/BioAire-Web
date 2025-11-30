$(function () {
    
    window.verSidebar = function () {
        var $s = $('.sidebar');
        if ($s.length) $s.css('display', 'block');
    };

    window.cerrarSidebar = function () {
        var $s = $('.sidebar');
        if ($s.length) $s.css('display', 'none');
    };

    window.aumentar = function () {
        var $input = $('.selector input');
        if (!$input.length) return;
        var min = parseInt($input.attr('min')) || 1;
        var max = parseInt($input.attr('max')) || 100;
        var val = parseInt($input.val()) || min;
        if (val < max) $input.val(val + 1);
    };

    window.quitar = function () {
        var $input = $('.selector input');
        if (!$input.length) return;
        var min = parseInt($input.attr('min')) || 1;
        var val = parseInt($input.val()) || min;
        if (val > min) $input.val(val - 1);
    };

    window.agregarComentario = function (nombre, puntuacion, texto) {
        var $lista = $('.lista-comentarios');
        if (!$lista.length) return;

        var $coment = $('<div>').addClass('comentario');
        var $header = $('<div>').addClass('comentario-header');
        var $strong = $('<strong>').addClass('nombre-usuario').text(nombre || 'Anónimo');
        var fecha = new Date().toLocaleDateString('es-PE', { day: 'numeric', month: 'long', year: 'numeric' });
        var $span = $('<span>').addClass('fecha').text(fecha);
        $header.append($strong).append($span);

        var stars = '';
        for (var i = 0; i < 5; i++) { stars += (i < Number(puntuacion || 0)) ? '★' : '☆'; }
        var $est = $('<div>').addClass('estrellas-comentario').text(stars);

        var $cuerpo = $('<p>').addClass('texto-comentario').text(texto || '');

        $coment.append($header).append($est).append($cuerpo);

        $lista.prepend($coment);
    };

    $('#nuevo-comentario').on('submit', function (e) {
        e.preventDefault();
        var nombre = $('#autor').val() || 'Anónimo';
        var puntuacion = $('#puntuacion').val() || '5';
        var texto = $('#texto').val() || '';
        if (!texto.trim()) return;
        agregarComentario(nombre.trim(), puntuacion, texto.trim());
        this.reset();
    });

    $('.barra').on('input', function () {
        var q = $(this).val().toLowerCase().trim();
        if (!q) {
            $('.productos > div').show();
            return;
        }
        $('.productos > div').each(function () {
            var $item = $(this);
            var text = ($item.text() || '').toLowerCase();
            if (text.indexOf(q) !== -1) $item.show(); else $item.hide();
        });
    });

    // Mostrar contacto (teléfono + email aleatorio) — llamado desde onclick del botón
    window.mostrarContacto = function () {
        var $btn = $('.contacto-adicional .centrar button');
        if (!$btn.length) return;
        var $existing = $('#contacto-info');
        if ($existing.length) { $existing.toggle(); return; }
       
        var telefono = '+51 9423534523';
        var email = 'user@bioaire.test';

        var $box = $('<div id="contacto-info">').css({ 'margin-top': '1rem', 'text-align': 'center', 'border-radius': '8px', 'background':'transparent', 'width': '20vw', 'height':'20vh', 'font-size':'medium', 'display':'flex', 'flex-direction':'column', 'align-items':'center'});
        $box.append($('<p>').html('<strong>Teléfono:</strong> ' + telefono));
        $box.append($('<p>').html('<strong>Correo Electrónico:</strong> ' + email));

        $btn.after($box);
    };
});