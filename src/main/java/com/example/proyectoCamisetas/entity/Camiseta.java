package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; 

@Entity
@Data
@NoArgsConstructor
@Table(name = "camiseta") 
public class Camiseta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(length = 50)
    private String marca; 

    private String talla; 

    @Column(length = 50)
    private String color;

    private String sexo; 

    private BigDecimal precio; 

    private Integer stock;

    private Boolean activo;

    // --- CORRECCIÓN AQUÍ: DESCOMENTAR ESTO ---
    @ManyToOne
    @JoinColumn(name = "categoria_id") // Esto creará/usará una columna 'categoria_id' en la tabla camiseta
    private Categoria categoria;
}