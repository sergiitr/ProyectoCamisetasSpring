package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; // Importamos todo para no fallar

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    @Autowired
    private CamisetaRepository camisetaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Home del admin
    @GetMapping({"", "/"}) 
    public String adminHome() {
        return "admin/index"; 
    }

    // Redirección
    @GetMapping("/camiseta") 
    public String adminCamisetas() {
        return "redirect:/admin/camiseta/list";
    }

    // 1. LISTAR
    @GetMapping("/camiseta/list") 
    public String listCamisetas(Model model) { 
        List<Camiseta> lista = camisetaRepository.findAll();
        model.addAttribute("camisetas", lista);
        return "admin/camiseta/list";
    }

    // 2. EDITAR
    @GetMapping("/camiseta/edit/{id}")
    public String editCamisetaForm(@PathVariable("id") Integer id, Model model) {
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            // Pasamos las categorías para el desplegable
            model.addAttribute("listaCategorias", categoriaRepository.findAll());
            return "admin/camiseta/edit"; 
        } else
            return "redirect:/admin/camiseta/list";
    }

    @PostMapping("/camiseta/save")
    public String saveCamiseta(@ModelAttribute Camiseta camiseta, @RequestParam(value = "categoriaId", required = false) Integer categoriaId) { 
        // 1. COMPROBACIÓN DE SEGURIDAD
        // Imprimimos en consola para ver si llega el ID (puedes verlo en tu terminal)
        System.out.println("Intentando guardar camiseta con ID: " + camiseta.getId());
        System.out.println("Nombre: " + camiseta.getNombre());
        
        // 2. ASIGNAR CATEGORÍA
        // Buscamos la categoría por el ID que viene del select y se la ponemos a la camiseta
        if (categoriaId != null) {
            Categoria categoriaSeleccionada = categoriaRepository.findById(categoriaId).orElse(null);
            camiseta.setCategoria(categoriaSeleccionada);
        }

        // 3. ACTUALIZAR EN BBDD
        // Al tener camiseta.getId() relleno (gracias al input hidden),
        // .save() hará un UPDATE en la base de datos automáticamente.
        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta/list";
    }
}