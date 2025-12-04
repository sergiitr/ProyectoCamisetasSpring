package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    
    @OneToMany(mappedBy = "categoria")
    @ToString.Exclude
    private List<Camiseta> camisetas = new ArrayList<>();
}