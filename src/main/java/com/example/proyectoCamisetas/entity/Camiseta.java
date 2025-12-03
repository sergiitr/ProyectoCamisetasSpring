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

    private String nombre;
    private String descripcion;
    
    @Column(length = 50)
    private String marca;
    private String talla;
    @Column(length = 50)
    private String color;
    private String sexo;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;

    // RELACIÓN CORRECTA: ManyToOne (Singular)
    @ManyToOne 
    @JoinColumn(name = "categoria_id") // Esta columna debe existir en tu BBDD
    @ToString.Exclude
    private Categoria categoria; 
}