package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Camiseta> camisetas;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Camiseta> getCamisetas() { return camisetas; }
    public void setCamisetas(List<Camiseta> camisetas) { this.camisetas = camisetas; }
}