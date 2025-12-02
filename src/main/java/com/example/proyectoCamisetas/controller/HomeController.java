package com.example.proyectoCamisetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody; // Opcional, para devolver solo texto

@Controller // Indica que esta clase es un controlador
public class HomeController {

    // Mapea la ruta raíz (http://localhost:8080/)
    @GetMapping("/")
    public String home() {
        // Devuelve el nombre de la plantilla (archivo HTML) a renderizar.
        // Spring buscará: src/main/resources/templates/index.html
        return "index"; 
    }

    /*
    // Si solo quieres probar que funciona devolviendo texto (sin Thymeleaf/HTML):
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "¡La aplicación Spring Boot funciona y se conecta a la base de datos!";
    }
    */
}