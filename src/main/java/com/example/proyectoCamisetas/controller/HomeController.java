// Archivo: src/main/java/com/example/proyectoCamisetas/controller/HomeController.java

package com.example.proyectoCamisetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * CORRECCIÓN: Maneja la ruta raíz (http://localhost:8080/)
     * y REDIRIGE a /admin, ya que es la primera página real de la aplicación.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/admin"; 
    }

    // El resto del código que tenías está bien para prueba.
    /*
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "¡La aplicación Spring Boot funciona y se conecta a la base de datos!";
    }
    */
}