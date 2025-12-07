// src/main/java/com/example/proyectoCamisetas/entity/Usuario.java
package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuario")
@Data // Genera getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    public enum Tipo {
        OPERADOR,
        CLIENTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    private String telefono;
    private String direccion;
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('OPERADOR','CLIENTE')")
    private Tipo tipo;
}