// Archivo: src/main/java/com/example/proyectoCamisetas/controller/AdminController.java

package com.example.proyectoCamisetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // Las peticiones que empiecen por /admin vendrán aquí
public class AdminController {

    // Este método maneja GETs a http://localhost:8080/admin
    @GetMapping({"", "/"}) 
    public String adminHome() {
        // Busca y renderiza la plantilla en: src/main/resources/templates/admin/index.html
        return "admin/index"; 
    }

    // Si tuvieras un panel de gestión de productos:
    // Este método maneja GETs a http://localhost:8080/admin/productos
    @GetMapping("/productos") 
    public String adminProductos() {
        // Busca y renderiza la plantilla en: src/main/resources/templates/admin/productos.html
        return "admin/productos";
    }
}