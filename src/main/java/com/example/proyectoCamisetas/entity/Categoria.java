package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;

    // Relación recursiva para subcategorías
    @ManyToOne 
    private Categoria padre;

    // Relación uno a muchos con Camiseta (Una categoría tiene muchas camisetas)
    // El atributo "categoria" en la clase Camiseta es el dueño de la relación.
    @OneToMany(mappedBy = "categoria")
    private List<Camiseta> camisetas; 

    // Constructor sin la lista de camisetas (para formularios)
    public Categoria(Long id, String nombre, String descripcion, Categoria padre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.padre = padre;
    }
}