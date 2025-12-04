package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; // Necesario para evitar bucles de referencia con Lombok
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categoria") 
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    private String nombre;
    
    // RELACIÓN INVERSA: UNA categoría tiene MUCHAS camisetas
    @OneToMany(mappedBy = "categoria") // 'categoria' es el campo en la entidad Camiseta.java
    @ToString.Exclude // Evita StackOverflowError cuando Lombok llama a toString()
    private List<Camiseta> camisetas = new ArrayList<>();
}