package com.bioaire.bioaire.controllers;

import com.bioaire.bioaire.models.Producto;
import com.bioaire.bioaire.models.Comentario;
import com.bioaire.bioaire.repositories.ProductoRepository;
import com.bioaire.bioaire.repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @GetMapping("/catalogo")
    public String catalogo(Model model, @RequestParam(required = false) String query) {
        List<Producto> productos;
        if (query != null && !query.isEmpty()) {
            productos = productoRepository.findByNombreContainingIgnoreCase(query);
        } else {
            productos = productoRepository.findAll();
        }
        
        productos.forEach(p -> {
            if (p.getImagenUrl() == null || p.getImagenUrl().isEmpty()) {
                p.setImagenUrl("/public/img/fan1.png");
            }
        });

        model.addAttribute("productos", productos);
        return "catalogo";
    }
    
    @GetMapping("/catalogo.html")
    public String catalogoAlias(Model model, @RequestParam(required = false) String query) {
        return catalogo(model, query);
    }

    @GetMapping("/producto/{id}")
    public String producto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/404";
        }
        
        if (producto.getImagenUrl() == null || producto.getImagenUrl().isEmpty()) {
            producto.setImagenUrl("/public/img/fan1.png");
        }

        model.addAttribute("producto", producto);

        List<Producto> relacionados = productoRepository.findAll().stream().limit(4).collect(Collectors.toList());
        relacionados.forEach(p -> {
             if (p.getImagenUrl() == null || p.getImagenUrl().isEmpty()) {
                p.setImagenUrl("/public/img/fan1.png");
            }
        });

        var comentarios = comentarioRepository.findByProducto(producto);
        float promedio = 0f;
        int totalComentarios = comentarios.size();
        if (!comentarios.isEmpty()) {
            promedio = Math.round((float) (comentarios.stream()
                .mapToInt(Comentario::getPuntuacion)
                .average()
                .orElse(0)) * 100f) / 100f;
        }
        model.addAttribute("totalComentarios", totalComentarios);
        model.addAttribute("promedio", promedio);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("relacionados", relacionados);
        return "producto";
    }
    
    @GetMapping("/producto.html")
    public String productoHtml(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            return producto(id, model);
        }
        return "redirect:/catalogo";
    }

    @PostMapping("/producto/{id}/comentario")
    public String agregarComentario(@PathVariable Long id, @RequestParam String autor, @RequestParam Integer puntuacion, @RequestParam String texto) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            Comentario comentario = new Comentario();
            comentario.setAutor(autor);
            comentario.setPuntuacion(puntuacion);
            comentario.setTexto(texto);
            comentario.setFecha(LocalDate.now());
            comentario.setProducto(producto);
            comentarioRepository.save(comentario);
        }
        return "redirect:/producto/" + id;
    }
}
