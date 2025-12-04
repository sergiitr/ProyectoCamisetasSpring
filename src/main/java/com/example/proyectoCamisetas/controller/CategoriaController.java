package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager; 
import jakarta.persistence.PersistenceContext; 
import java.util.Optional; 

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepo;
    
    @PersistenceContext
    private EntityManager entityManager; 

    // Listar
    @GetMapping({"", "/", "/list"})
    public String list(Model model) {
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "admin/categoria/list";
    }

    // Añadir 
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria/add";
    }

    // Procesar añadir 
    @PostMapping("/add")
    public String add(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria"; 
    }

    // Editar 
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow()); 
        return "admin/categoria/edit";
    }

    // Procesar editar
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria";
    }

    // Confirmar borrar 
    @GetMapping("/del/{id}")
    public String delForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow());
        return "admin/categoria/del";
    }

    @PostMapping("/del/{id}")
    @Transactional
    public String del(@PathVariable Integer id) {
        Categoria categoria = categoriaRepo.findById(id).orElse(null);
        if (categoria != null) {
            entityManager.remove(categoria);
            entityManager.flush(); 
        }
        
        return "redirect:/admin/categoria";
    }

    @GetMapping("/{id}/camisetas")
    public String detalle(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        model.addAttribute("categoria", categoria);
        model.addAttribute("camisetas", categoria.getCamisetas()); 
        return "admin/categoria/detalle";
    }
}