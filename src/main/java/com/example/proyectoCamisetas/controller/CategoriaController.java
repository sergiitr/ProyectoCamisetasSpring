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

/**
 * Controlador para la gestión de la entidad Categoria.
 * Maneja todas las operaciones CRUD y la vista Maestro-Detalle de las categorías.
 */
@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepo;  
    @PersistenceContext
    private EntityManager entityManager; 
    
    /**
     * Muestra el listado completo de categorías.
     * Mapea: GET /admin/categoria o GET /admin/categoria/list
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de listado (list.html).
     */
    @GetMapping({"", "/", "/list"})
    public String list(Model model) {
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "admin/categoria/list";
    }

    /**
     * Muestra el formulario para añadir una nueva categoría.
     * Mapea: GET /admin/categoria/add
     * @param model Contenedor de datos para la vista (pasa un objeto Categoria vacío).
     * @return Nombre de la plantilla de adición (add.html).
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria/add";
    }

    /**
     * Procesa la inserción de una nueva categoría.
     * Mapea: POST /admin/categoria/add
     * @param categoria Objeto Categoria poblado por el formulario.
     * @return Redirección al listado principal.
     */
    @PostMapping("/add")
    public String add(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria"; 
    }

    /**
     * Muestra el formulario para editar una categoría existente.
     * Mapea: GET /admin/categoria/edit/{id}
     * @param id ID de la categoría a editar.
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de edición (edit.html).
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow()); 
        return "admin/categoria/edit";
    }

    /**
     * Procesa la actualización de una categoría existente.
     * Mapea: POST /admin/categoria/edit/{id}
     * @param categoria Objeto Categoria poblado por el formulario (debe contener el ID).
     * @return Redirección al listado principal.
     */
    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categoria";
    }

    /**
     * Muestra la página de confirmación de borrado.
     * Mapea: GET /admin/categoria/del/{id}
     * @param id ID de la categoría a borrar.
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de confirmación (del.html).
     */
    @GetMapping("/del/{id}")
    public String delForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepo.findById(id).orElseThrow());
        return "admin/categoria/del";
    }

    /**
     * Ejecuta la eliminación de la categoría.
     * Mapea: POST /admin/categoria/del/{id}
     * @param id ID de la categoría a borrar.
     * @return Redirección al listado principal.
     */
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

    /**
     * Muestra la vista Maestro-Detalle, listando todas las camisetas asociadas a una categoría.
     * Mapea: GET /admin/categoria/{id}/camisetas
     * @param id ID de la categoría.
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de detalle (detalle.html).
     */
    @GetMapping("/{id}/camisetas")
    public String detalle(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        model.addAttribute("categoria", categoria);
        // Obtiene la lista de camisetas asociadas a través de la relación @OneToMany
        model.addAttribute("camisetas", categoria.getCamisetas()); 
        return "admin/categoria/detalle";
    }
}