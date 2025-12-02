package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/camiseta")
public class CamisetaController {

    @Autowired
    private CamisetaRepository camisetaRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("camisetas", camisetaRepo.findAll());
        return "camiseta/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("camiseta", new Camiseta());
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "camiseta/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Camiseta camiseta) {
        camisetaRepo.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("camiseta", camisetaRepo.findById(id).orElseThrow());
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "camiseta/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute Camiseta camiseta) {
        camisetaRepo.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    @GetMapping("/del/{id}")
    public String delForm(@PathVariable Integer id, Model model) {
        model.addAttribute("camiseta", camisetaRepo.findById(id).orElseThrow());
        return "camiseta/del";
    }

    @PostMapping("/del/{id}")
    public String del(@PathVariable Integer id) {
        camisetaRepo.deleteById(id);
        return "redirect:/admin/camiseta";
    }
}
