package com.bioaire.bioaire.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String autor;
    
    @Column(length = 500)
    private String texto;
    
    private Integer puntuacion;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "producto_id", foreignKey = @ForeignKey(name = "FK_Producto_Comentario"))
    private Producto producto;
}
