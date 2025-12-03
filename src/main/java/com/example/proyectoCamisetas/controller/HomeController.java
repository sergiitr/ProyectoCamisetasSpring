// Archivo: src/main/java/com/example/proyectoCamisetas/controller/HomeController.java

package com.example.proyectoCamisetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Maneja la ruta raíz (http://localhost:8080/) y REDIRIGE a /admin, ya que es la primera página real de la aplicación.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/admin"; 
    }
}