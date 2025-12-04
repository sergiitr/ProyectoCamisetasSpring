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
    
    @PersistenceContext
    private EntityManager entityManager; 

    @GetMapping({"", "/"}) public String adminHome() { return "admin/index"; }
    @GetMapping("/camiseta") public String adminCamisetas() { return "redirect:/admin/camiseta/list"; }

    @GetMapping("/camiseta/list") 
    public String listCamisetas(Model model) { 
        List<Camiseta> lista = camisetaRepository.findAll();
        model.addAttribute("camisetas", lista);
        return "admin/camiseta/list";
    }

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

    @PostMapping("/camiseta/save")
    public String saveCamiseta(@ModelAttribute Camiseta camiseta, @RequestParam(value = "categoriaId", required = false) Integer categoriaId) { 
        
        // Asigno Categoría de forma manual para evitar errores de conversión
        if (categoriaId != null) {
            Categoria categoriaSeleccionada = categoriaRepository.findById(categoriaId).orElse(null);
            camiseta.setCategoria(categoriaSeleccionada);
        }

        // Actualizo o creo en la BBDD
        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta/list";
    }

    @GetMapping("/camiseta/del/{id}")
    public String deleteCamisetaForm(@PathVariable("id") Integer id, Model model) {
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            return "admin/camiseta/del"; 
        } else
            return "redirect:/admin/camiseta/list";
    }

    @PostMapping("/camiseta/del/{id}")
    @Transactional
    public String deleteCamisetaData(@PathVariable("id") Integer id) {
        Camiseta camiseta = camisetaRepository.findById(id).orElse(null);
        if (camiseta != null) {
            entityManager.remove(camiseta);
            entityManager.flush(); 
        }
        return "redirect:/admin/camiseta/list";
    }
}