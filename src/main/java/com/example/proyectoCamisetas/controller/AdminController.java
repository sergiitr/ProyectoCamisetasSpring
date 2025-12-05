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
import org.springframework.web.servlet.view.RedirectView;

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

    // Ir al index
    @GetMapping({"", "/"})
    public String adminHome() {
        return "admin/index";
    }

    // Muestra lista de camisetas
    @GetMapping("/camiseta")
    public String adminCamisetas() {
        return "redirect:/admin/camiseta/list";
    }

    @GetMapping("/camiseta/list") 
    public String listCamisetas(Model model) { 
        List<Camiseta> lista = camisetaRepository.findAll();
        model.addAttribute("camisetas", lista);
        return "admin/camiseta/list";
    }

    // Añadimos camiseta
    @GetMapping("/camiseta/add")
    public String addCamisetaForm(Model model) {
        model.addAttribute("camiseta", new Camiseta()); 
        model.addAttribute("listaCategorias", categoriaRepository.findAll()); 
        return "admin/camiseta/add";
    }

    @PostMapping({"/camiseta/add"})
    public String saveCamiseta(@ModelAttribute Camiseta camiseta, @RequestParam(value = "categoriaId", required = false) Integer categoriaId) { 
        if (categoriaId != null) {
            Categoria categoriaSeleccionada = categoriaRepository.findById(categoriaId).orElse(null);
            camiseta.setCategoria(categoriaSeleccionada);
        }

        camisetaRepository.save(camiseta);
        return "redirect:/admin/camiseta/list";
    }

    // Editamos camiseta
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

    // Borramos camiseta
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

    @GetMapping("/maestro_detalle_camisetas")
    public String maestroDetalleCamisetas(@RequestParam(value = "categoriaId", required = false) Integer categoriaId, Model model) {
        List<Camiseta> camisetas;
        Categoria categoriaSeleccionada = null;

        List<Categoria> todasLasCategorias = categoriaRepository.findAll();
        model.addAttribute("categorias", todasLasCategorias);

        if (categoriaId != null) {
            Optional<Categoria> oCat = categoriaRepository.findById(categoriaId);
            if (oCat.isPresent()) {
                categoriaSeleccionada = oCat.get();
                camisetas = categoriaSeleccionada.getCamisetas(); 
            } else 
                camisetas = camisetaRepository.findAll(); 
        } else 
            camisetas = camisetaRepository.findAll();
        
        model.addAttribute("camisetas", camisetas);
        model.addAttribute("categoriaSeleccionada", categoriaSeleccionada);

        return "admin/maestro_detalle_camisetas"; 
    }
}
