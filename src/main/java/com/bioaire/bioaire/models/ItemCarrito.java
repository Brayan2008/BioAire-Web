package com.bioaire.bioaire.models;

import lombok.Data;

@Data
public class ItemCarrito {
    private Producto producto;
    private Integer cantidad;

    public Double getTotal() {
        return producto.getPrecio() * cantidad;
    }
}
