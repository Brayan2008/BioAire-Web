package com.bioaire.bioaire.controllers;

import com.bioaire.bioaire.models.Producto;
import com.bioaire.bioaire.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoRepository productoRepository;

    private final Path rootLocation = Paths.get("uploads");

    public AdminController() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @GetMapping("/producto/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "admin/nuevo_producto"; 
    }

    @PostMapping("/producto/guardar")
    public String guardarProducto(Producto producto, 
                                  @RequestParam("imagen1") MultipartFile imagen1,
                                  @RequestParam(value = "imagen2", required = false) MultipartFile imagen2,
                                  @RequestParam(value = "imagen3", required = false) MultipartFile imagen3) {
        
        String defaultImg = "/public/img/fan1.png";
                                    
        if (imagen1 != null && !imagen1.isEmpty()) {
            producto.setImagenUrl(saveFile(imagen1));
        } else {
            producto.setImagenUrl(defaultImg);
        }

        if (imagen2 != null && !imagen2.isEmpty()) {
            producto.setImagenUrl2(saveFile(imagen2));
        }

        if (imagen3 != null && !imagen3.isEmpty()) {
            producto.setImagenUrl3(saveFile(imagen3));
        }

        productoRepository.save(producto);
        return "redirect:/catalogo";
    }

    private String saveFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
