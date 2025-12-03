package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    // 1. Inyectamos el repositorio para poder usar la Base de Datos
    @Autowired
    private CamisetaRepository camisetaRepository;

    @GetMapping({"", "/"}) 
    public String adminHome() {
        return "admin/index"; 
    }

    // Si entran a /admin/camiseta, mejor redirigirlos a la lista directamente
    @GetMapping("/camiseta") 
    public String adminCamisetas() {
        return "redirect:/admin/camiseta/list";
    }

    // 2. Método para listar (CORREGIDO)
    @GetMapping("/camiseta/list") 
    public String listCamisetas(Model model) { // Se añade "Model model" aquí
        // Obtenemos la lista de la BD
        List<Camiseta> lista = camisetaRepository.findAll();
        
        // Pasamos la lista a la vista
        model.addAttribute("camisetas", lista);
        
        // 3. Renderiza: src/main/resources/templates/admin/camiseta/list.html
        return "admin/camiseta/list";
    }
}