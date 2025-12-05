package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager; 
import jakarta.persistence.PersistenceContext; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador principal para la administración de Camisetas y el panel de control general.
 * Maneja todas las operaciones CRUD y las vistas maestras.
 */
@Controller
@RequestMapping("/admin") 
public class AdminController {

    @Autowired
    private CamisetaRepository camisetaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @PersistenceContext
    private EntityManager entityManager; 

    /**
     * Muestra la página principal del panel de administración.
     * Mapea: GET /admin o GET /admin/
     * @return Nombre de la plantilla principal (index.html)
     */
    @GetMapping({"", "/"})
    public String adminHome() {
        return "admin/index";
    }

    /**
     * Redirige la URL base de camisetas a la lista principal.
     * Mapea: GET /admin/camiseta
     * @return Redirección a /admin/camiseta/list
     */
    @GetMapping("/camiseta")
    public String adminCamisetas() {
        return "redirect:/admin/camiseta/list";
    }

    /**
     * Muestra el listado completo de camisetas.
     * Mapea: GET /admin/camiseta/list
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de listado de camisetas.
     */
    @GetMapping("/camiseta/list") 
    public String listCamisetas(Model model) { 
        List<Camiseta> lista = camisetaRepository.findAll();
        model.addAttribute("camisetas", lista);
        return "admin/camiseta/list";
    }

    /**
     * Muestra el formulario para añadir una nueva camiseta.
     * Mapea: GET /admin/camiseta/add
     * @param model Contenedor de datos para la vista (pasa objeto Camiseta vacío y lista de Categorias).
     * @return Nombre de la plantilla de adición de camiseta.
     */
    @GetMapping("/camiseta/add")
    public String addCamisetaForm(Model model) {
        model.addAttribute("camiseta", new Camiseta()); 
        model.addAttribute("listaCategorias", categoriaRepository.findAll()); 
        return "admin/camiseta/add";
    }

    /**
     * Procesa la creación/actualización de una camiseta desde el formulario.
     * Mapea: POST /admin/camiseta/add (Maneja tanto ADD como SAVE/UPDATE)
     * @param camiseta Objeto Camiseta poblado por el formulario.
     * @param categoriaId ID de la categoría seleccionada (recibido por separado).
     * @return Redirección al listado.
     */
    @PostMapping({"/camiseta/add", "/camiseta/save"})
    public String saveCamiseta(@ModelAttribute Camiseta camiseta, @RequestParam(value = "categoriaId", required = false) Integer categoriaId) { 
        if (categoriaId != null) {
            Categoria categoriaSeleccionada = categoriaRepository.findById(categoriaId).orElse(null);
            camiseta.setCategoria(categoriaSeleccionada);
        }

        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta/list";
    }

    /**
     * Muestra el formulario para editar una camiseta existente.
     * Mapea: GET /admin/camiseta/edit/{id}
     * @param id ID de la camiseta a editar.
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de edición o redirección si no existe.
     */
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

    /**
     * Muestra la página de confirmación de borrado para una camiseta.
     * Mapea: GET /admin/camiseta/del/{id}
     * @param id ID de la camiseta a borrar.
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla de confirmación o redirección si no existe.
     */
    @GetMapping("/camiseta/del/{id}")
    public String deleteCamisetaForm(@PathVariable("id") Integer id, Model model) {
        Optional<Camiseta> oCamiseta = camisetaRepository.findById(id);
        if (oCamiseta.isPresent()) {
            model.addAttribute("camiseta", oCamiseta.get());
            return "admin/camiseta/del"; 
        } else 
            return "redirect:/admin/camiseta/list";
    }

    /**
     * Ejecuta la operación de borrado de una camiseta.
     * Mapea: POST /admin/camiseta/del/{id}
     * @param id ID de la camiseta a borrar.
     * @return Redirección al listado de camisetas.
     */
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

    /**
     * Muestra la vista Maestro-Detalle con la opción de filtrar camisetas por categoría.
     * Mapea: GET /admin/maestro_detalle_camisetas
     * @param categoriaId ID de la categoría para filtrar (opcional).
     * @param model Contenedor de datos para la vista.
     * @return Nombre de la plantilla Maestro-Detalle.
     */
    @GetMapping("/maestro_detalle_camisetas")
    public String maestroDetalleCamisetas(@RequestParam(value = "categoriaId", required = false) Integer categoriaId, Model model) {
        List<Camiseta> camisetas;
        Categoria categoriaSeleccionada = null;

        // Cargar todas las categorías para el filtro SELECT
        List<Categoria> todasLasCategorias = categoriaRepository.findAll();
        model.addAttribute("categorias", todasLasCategorias);

        // Lógica de Filtrado
        if (categoriaId != null) {
            Optional<Categoria> oCat = categoriaRepository.findById(categoriaId);
            if (oCat.isPresent()) {
                categoriaSeleccionada = oCat.get();
                camisetas = categoriaSeleccionada.getCamisetas(); 
            } else 
                camisetas = camisetaRepository.findAll(); 
        } else
            camisetas = camisetaRepository.findAll();   // Si no hay parámetro de filtro, lista todas
        
        model.addAttribute("camisetas", camisetas);
        model.addAttribute("categoriaSeleccionada", categoriaSeleccionada);

        // Retorna la plantilla Maestro-Detalle
        return "admin/maestro_detalle_camisetas"; 
    }
}