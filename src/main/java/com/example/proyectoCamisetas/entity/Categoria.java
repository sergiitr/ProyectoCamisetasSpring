package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
// Si quieres relación bidireccional:
// import java.util.List;
// import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categoria") // Asegúrate de que tu tabla se llame así
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // O Integer, según tu BBDD

    private String nombre;
    
    // Opcional: descripción, activa, etc.
    
    // NOTA: No es obligatorio poner la lista de camisetas aquí para que funcione el formulario.
    // Si la pones, recuerda usar @OneToMany(mappedBy = "categoria") y @ToString.Exclude
}