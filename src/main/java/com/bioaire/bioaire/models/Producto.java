package com.bioaire.bioaire.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    @Column(length = 1000)
    private String descripcion;
    
    private Double precio;

    private Double precioPostDescuento;
  
    private Integer descuento;
    
    private String imagenUrl;

    private String imagenUrl2;

    private String imagenUrl3;
  
    private Integer stock;
    
    private String categoria;
  
    private Double velocidad;
  
    private Integer garantia;

    private String marca;
  
    private String modelo;
  
    private Integer numeroAspas;
  
    private Double potencia;
  
    private Double diametro;
  
    private String tipoInstalacion;
  
    private String material;
  
    private Double voltaje;
  
    private Boolean controlRemoto;
  
    private Double peso;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private final List<Comentario> comentarios = new ArrayList<>();

    @PrePersist
    public void preSave() {
        if (descuento != 0 || descuento != null) {
            precioPostDescuento = precio - (precio*descuento/100);
        } else {
            precioPostDescuento = precio;
        }
    }


}