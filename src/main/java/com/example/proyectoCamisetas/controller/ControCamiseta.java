// Archivo: src/main/java/com/example/proyectoCamisetas/controller/ControCamiseta.java

package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CamisetaRepository; 
import com.example.proyectoCamisetas.repository.CategoriaRepository; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/camiseta")
public class ControCamiseta {

    @Autowired
    CamisetaRepository camisetaRepository; 

    @Autowired
    CategoriaRepository categoriaRepository; 


    // RUTA: GET /admin/camiseta -> findAll()
    @GetMapping({"", "/"})
    public String findAll(Model model) {
        model.addAttribute("camisetas", camisetaRepository.findAll());
        return "camiseta/list"; 
    }

    // RUTA: GET /admin/camiseta/add -> addCamisetaForm()
    @GetMapping("/add")
    public String addCamisetaForm(Model model) {
        model.addAttribute("camiseta", new Camiseta());
        model.addAttribute("categorias", categoriaRepository.findAll()); 
        return "camiseta/add";
    }

    // RUTA: POST /admin/camiseta/add -> addCamiseta()
    @PostMapping("/add")
    public String addCamiseta(@ModelAttribute("camiseta") Camiseta camiseta) {
        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    // RUTA: GET /admin/camiseta/edit/{id} -> editCamisetaForm()
    // CORRECCIÓN: Cambiamos Long a Integer para que coincida con el Repositorio de Categoria
    @GetMapping("/edit/{id}")
    public String editCamisetaForm(@PathVariable("id") Integer id, Model model) {
        // ASUMIMOS que CamisetaRepository también usa Integer
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "camiseta/edit"; 
        } else {
            model.addAttribute("titulo", "Error de Edición");
            model.addAttribute("mensaje", "Camiseta no encontrada para editar.");
            return "error"; 
        }
    }

    // RUTA: POST /admin/camiseta/edit/{id} -> editCamiseta()
    @PostMapping("/edit/{id}") 
    public String editCamiseta(@ModelAttribute("camiseta") Camiseta camiseta) {
        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    // RUTA: GET /admin/camiseta/del/{id} -> delCamisetaForm()
    @GetMapping("/del/{id}")
    public String delCamisetaForm(@PathVariable("id") Integer id, Model model) {
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            return "camiseta/del"; 
        } else {
            model.addAttribute("titulo", "Error de Borrado");
            model.addAttribute("mensaje", "Camiseta no encontrada para borrado.");
            return "error";
        }
    }

    // RUTA: POST /admin/camiseta/del/{id} -> delCamiseta()
    @PostMapping("/del/{id}")
    public String delCamiseta(@PathVariable("id") Integer id) {
        camisetaRepository.deleteById(id);
        return "redirect:/admin/camiseta";
    }

    // RUTA PARA MAESTRO-DETALLE: GET /admin/camiseta/categoria/{id}
    @GetMapping("/categoria/{id}")
    public String findByCategoria(@PathVariable("id") Integer id, Model model) {
        // Ahora el id es Integer, que es lo que espera CategoriaRepository
        Optional<Categoria> oCategoria = categoriaRepository.findById(id); 
        if (oCategoria.isPresent()) {
            Categoria categoria = oCategoria.get();
            // Esta línea asume que tu repositorio tiene un método customizado:
            List<Camiseta> camisetas = camisetaRepository.findByCategoria(categoria); 
            
            model.addAttribute("categorias", categoriaRepository.findAll()); 
            model.addAttribute("categoriaSeleccionada", categoria);   
            model.addAttribute("camisetas", camisetas);               
            
            return "admin/maestro-detalle-camisetas"; 
        } else {
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", "Categoría no encontrada.");
            return "error";
        }
    }
}