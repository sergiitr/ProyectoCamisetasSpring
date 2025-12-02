package com.example.proyectoCamisetas.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "camiseta")
public class Camiseta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Talla talla;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String color;
    private String marca;

    private Integer stock;

    private BigDecimal precio;

    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Talla getTalla() { return talla; }
    public void setTalla(Talla talla) { this.talla = talla; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}