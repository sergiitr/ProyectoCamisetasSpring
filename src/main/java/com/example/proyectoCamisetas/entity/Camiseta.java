package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "camiseta")
public class Camiseta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campos añadidos según el esquema de BBDD
    private String nombre;
    private String descripcion;
    
    @Column(length = 50)
    private String marca;
    private String talla;
    @Column(length = 50)
    private String color; // Campo NOT NULL
    private String sexo;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;

    // RELACIÓN Muchos a Uno (La corregida)
    @ManyToOne 
    @JoinColumn(name = "categoria_id") 
    @ToString.Exclude
    private Categoria categoria; 
}