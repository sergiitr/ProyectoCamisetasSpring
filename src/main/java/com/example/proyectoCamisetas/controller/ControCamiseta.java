package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
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
    CamisetaRepository CamisetaRepository;

    @Autowired
    CategoriaRepository CategoriaRepository;

    // RUTA: GET /admin/camiseta -> findAll()
    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("camisetas", CamisetaRepository.findAll());
        return "admin/camisetas-listado";
    }

    // RUTA: GET /admin/camiseta/add -> addCamisetaForm()
    @GetMapping("/add")
    public String addCamisetaForm(Model model) {
        model.addAttribute("camiseta", new Camiseta());
        // Necesario para el selector de categorías en el formulario
        model.addAttribute("categorias", CategoriaRepository.findAll()); 
        return "admin/camisetas-form";
    }

    // RUTA: POST /admin/camiseta/add -> addCamiseta()
    @PostMapping("/add")
    public String addCamiseta(@ModelAttribute("camiseta") Camiseta camiseta) {
        CamisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    // RUTA: GET /admin/camiseta/edit/{id} -> editCamisetaForm()
    @GetMapping("/edit/{id}")
    public String editCamisetaForm(@PathVariable("id") Long id, Model model) {
        Optional<Camiseta> oCamiseta = CamisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            model.addAttribute("categorias", CategoriaRepository.findAll());
            return "admin/camisetas-form";
        } else {
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", "Camiseta no encontrada.");
            return "error";
        }
    }

    // RUTA: POST /admin/camiseta/edit/{id} -> editCamiseta()
    // Nota: El método 'save' de JPA se usa para crear y actualizar. 
    // Si el objeto tiene ID, actualiza; si no, crea.
    @PostMapping("/edit/{id}") 
    public String editCamiseta(@ModelAttribute("camiseta") Camiseta camiseta) {
        CamisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta";
    }

    // RUTA: GET /admin/camiseta/del/{id} -> delCamisetaForm()
    @GetMapping("/del/{id}")
    public String delCamisetaForm(@PathVariable("id") Long id, Model model) {
        Optional<Camiseta> oCamiseta = CamisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            return "admin/camisetas-del"; // Vista de confirmación
        } else {
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", "Camiseta no encontrada.");
            return "error";
        }
    }

    // RUTA: POST /admin/camiseta/del/{id} -> delCamiseta()
    @PostMapping("/del/{id}")
    public String delCamiseta(@PathVariable("id") Long id) {
        CamisetaRepository.deleteById(id);
        return "redirect:/admin/camiseta";
    }

    // RUTA PARA MAESTRO-DETALLE
    // GET /admin/camiseta/categoria/{id}
    @GetMapping("/categoria/{id}")
    public String findByCategoria(@PathVariable("id") Long id, Model model) {
        Optional<Categoria> oCategoria = CategoriaRepository.findById(id);
        if (oCategoria.isPresent()) {
            Categoria categoria = oCategoria.get();
            // Utiliza el método de repositorio personalizado
            List<Camiseta> camisetas = CamisetaRepository.findByCategoria(categoria);
            
            model.addAttribute("categorias", CategoriaRepository.findAll()); // Lista de maestros
            model.addAttribute("categoriaSeleccionada", categoria);   // Maestro seleccionado
            model.addAttribute("camisetas", camisetas);               // Detalles (camisetas)
            
            return "admin/maestro-detalle-camisetas"; 
        } else {
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", "Categoría no encontrada.");
            return "error";
        }
    }

}