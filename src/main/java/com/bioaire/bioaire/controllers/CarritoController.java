package com.bioaire.bioaire.controllers;

import com.bioaire.bioaire.models.ItemCarrito;
import com.bioaire.bioaire.models.Producto;
import com.bioaire.bioaire.models.Cliente;
import com.bioaire.bioaire.models.Pedido;
import com.bioaire.bioaire.models.DetallePedido;
import com.bioaire.bioaire.repositories.ProductoRepository;
import com.bioaire.bioaire.repositories.ClienteRepository;
import com.bioaire.bioaire.repositories.PedidoRepository;
import com.bioaire.bioaire.repositories.DetallePedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CarritoController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> getCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        List<ItemCarrito> items = getCarrito(session);
        double total = items.stream().mapToDouble(ItemCarrito::getTotal).sum();
        
        // Ensure images are set for display
        items.forEach(item -> {
            if (item.getProducto().getImagenUrl() == null || item.getProducto().getImagenUrl().isEmpty()) {
                item.getProducto().setImagenUrl("/public/img/fan1.png");
            }
        });

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId, @RequestParam Integer cantidad, HttpSession session) {
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto != null) {
            List<ItemCarrito> items = getCarrito(session);
            Optional<ItemCarrito> existingItem = items.stream()
                    .filter(i -> i.getProducto().getId().equals(producto.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                existingItem.get().setCantidad(existingItem.get().getCantidad() + cantidad);
            } else {
                ItemCarrito newItem = new ItemCarrito();
                newItem.setProducto(producto);
                newItem.setCantidad(cantidad);
                items.add(newItem);
            }
        }
        return "redirect:/carrito";
    }

    @GetMapping("/carrito/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        List<ItemCarrito> items = getCarrito(session);
        items.removeIf(i -> i.getProducto().getId().equals(id));
        return "redirect:/carrito";
    }
    
    @GetMapping("/carrito/checkout")
    public String checkoutForm(Model model, HttpSession session) {
        List<ItemCarrito> items = getCarrito(session);
        if (items.isEmpty()) {
            return "redirect:/carrito";
        }
        double total = items.stream().mapToDouble(ItemCarrito::getTotal).sum();
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("cliente", new Cliente());
        
        return "checkout";
    }

    @PostMapping("/carrito/checkout")
    public String procesarCompra(Cliente cliente, HttpSession session) {
        List<ItemCarrito> items = getCarrito(session);
        if (items.isEmpty()) {
            return "redirect:/carrito";
        }
        
        // Save Cliente
        clienteRepository.save(cliente);
        
        // Create Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDateTime.now());
        pedido.setTotal(items.stream().mapToDouble(ItemCarrito::getTotal).sum());
        pedidoRepository.save(pedido);
        
        // Create Detalles
        for (ItemCarrito item : items) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            detalle.setSubtotal(item.getTotal());
            detallePedidoRepository.save(detalle);
        }
        
        // Clear Cart
        items.clear();
        
        return "redirect:/?compraExitosa=true";
    }
}
