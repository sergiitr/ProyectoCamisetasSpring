package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional; // Necesario para findById y orElseThrow

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepo;

    // 1. LISTAR (GET /admin/categoria y /admin/categoria/list)
    // Maneja la ruta base y la ruta /list para listar las categorías.
    @GetMapping({"", "/", "/list"})
    public String list(Model model) {
        // En lugar de redirigir en la ruta base, servimos la lista directamente
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "admin/categoria/list";
    }

    // 2. AÑADIR (GET /admin/categoria/add)
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria/add";
    }

    // 3. PROCESAR AÑADIR (POST /admin/categoria/add)
    @PostMapping("/add")
    public String add(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        // Redirige a la ruta base /admin/categoria, que es manejada por el método list
        return "redirect:/admin/categoria"; 
    }

    // 4. EDITAR (GET /admin/categoria/edit/{id})
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        // Usamos orElseThrow() que lanza una excepción si no lo encuentra (Spring maneja el 404/500)
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow()); 
        return "admin/categoria/edit";
    }

    // 5. PROCESAR EDITAR (POST /admin/categoria/edit/{id})
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria";
    }

    // 6. CONFIRMAR BORRAR (GET /admin/categoria/del/{id})
    @GetMapping("/del/{id}")
    public String delForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow());
        return "admin/categoria/del";
    }

    // 7. EJECUTAR BORRAR (POST /admin/categoria/del/{id})
    @PostMapping("/del/{id}")
    public String del(@PathVariable Integer id) {
        // En un escenario real, debes manejar la llave foránea aquí
        categoriaRepo.deleteById(id);
        return "redirect:/admin/categoria";
    }

    // 8. MAESTRO-DETALLE (GET /admin/categoria/{id}/camisetas)
    @GetMapping("/{id}/camisetas")
    public String detalle(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        model.addAttribute("categoria", categoria);
        
        // Asumiendo que has corregido Categoria.java para incluir la lista de camisetas
        model.addAttribute("camisetas", categoria.getCamisetas()); 
        return "admin/categoria/detalle";
    }

    @GetMapping("/{id}/camisetas")
    public String detalle(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        model.addAttribute("categoria", categoria);
        // Esta línea funcionará porque Categoria.java ya tiene la lista @OneToMany
        model.addAttribute("camisetas", categoria.getCamisetas()); 
        return "admin/categoria/detalle"; // Apunta a la vista de arriba
    }
}