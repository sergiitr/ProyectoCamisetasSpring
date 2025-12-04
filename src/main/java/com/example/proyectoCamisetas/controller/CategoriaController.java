package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "admin/categoria/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow());
        return "admin/categoria/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria";
    }

    @GetMapping("/del/{id}")
    public String delForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow());
        return "admin/categoria/del";
    }

    @PostMapping("/del/{id}")
    public String del(@PathVariable Integer id) {
        categoriaRepo.deleteById(id);
        return "redirect:/admin/categoria";
    }

    // Maestro-detalle
    @GetMapping("/{id}/camisetas")
    public String detalle(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        model.addAttribute("categoria", categoria);
        model.addAttribute("camisetas", categoria.getCamisetas());
        return "admin/categoria/detalle";
    }
}