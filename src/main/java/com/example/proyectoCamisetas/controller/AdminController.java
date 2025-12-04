package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager; // <-- Nuevo Import
import jakarta.persistence.PersistenceContext; // <-- Nuevo Import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    @Autowired
    private CamisetaRepository camisetaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // INYECTAMOS ENTITY MANAGER para borrado robusto
    @PersistenceContext
    private EntityManager entityManager; 

    // Home del admin y Redirecciones
    @GetMapping({"", "/"}) public String adminHome() { return "admin/index"; }
    @GetMapping("/camiseta") public String adminCamisetas() { return "redirect:/admin/camiseta/list"; }

    // 1. LISTAR
    @GetMapping("/camiseta/list") 
    public String listCamisetas(Model model) { 
        List<Camiseta> lista = camisetaRepository.findAll();
        model.addAttribute("camisetas", lista);
        return "admin/camiseta/list";
    }

    // 2. EDITAR (GET)
    @GetMapping("/camiseta/edit/{id}")
    public String editCamisetaForm(@PathVariable("id") Integer id, Model model) {
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            model.addAttribute("listaCategorias", categoriaRepository.findAll());
            return "admin/camiseta/edit"; 
        } else
            return "redirect:/admin/camiseta/list";
    }

    // 3. GUARDAR (POST) - Lógica de actualización robusta
    @PostMapping("/camiseta/save")
    public String saveCamiseta(@ModelAttribute Camiseta camiseta, 
                               @RequestParam(value = "categoriaId", required = false) Integer categoriaId) { 
        
        // 1. Asignar Categoría de forma manual para evitar errores de conversión
        if (categoriaId != null) {
            Categoria categoriaSeleccionada = categoriaRepository.findById(categoriaId).orElse(null);
            camiseta.setCategoria(categoriaSeleccionada);
        }

        // 2. Actualizar o Crear en BBDD
        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta/list";
    }

    // 4. CONFIRMAR BORRADO (GET)
    @GetMapping("/camiseta/del/{id}")
    public String deleteCamisetaForm(@PathVariable("id") Integer id, Model model) {
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            return "admin/camiseta/del"; 
        } else {
            return "redirect:/admin/camiseta/list";
        }
    }

    // 5. EJECUTAR BORRADO (POST) - SOLUCIÓN A LA LLAVE FORÁNEA
    @PostMapping("/camiseta/del/{id}")
    @Transactional
    public String deleteCamisetaData(@PathVariable("id") Integer id) {
        // En lugar de deleteById (que fallaba), traemos la entidad y la borramos
        // usando el EntityManager para gestionar mejor las dependencias huérfanas
        // de la tabla camiseta_categoria que aún existe.
        Camiseta camiseta = camisetaRepository.findById(id).orElse(null);
        
        if (camiseta != null) {
            entityManager.remove(camiseta);
            // Esto es crucial para forzar la sincronización y la limpieza de relaciones huérfanas
            // ANTES de que la transacción termine y falle el commit.
            entityManager.flush(); 
        }
        
        return "redirect:/admin/camiseta/list";
    }
}