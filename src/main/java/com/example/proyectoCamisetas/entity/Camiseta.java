package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Camiseta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String talla;
    private Float precio;
    
    // Relación muchos a uno con Categoría (Muchas camisetas pertenecen a una categoría)
    @ManyToOne
    private Categoria categoria;
}