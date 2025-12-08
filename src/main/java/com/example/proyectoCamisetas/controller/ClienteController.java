package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private CamisetaRepository camisetaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping({"", "/"})
    public String clienteHome() {
        return "cliente/index"; 
    }

    @GetMapping("/camisetas/list")
    public String listCamisetas(Model model) {
        List<Camiseta> camisetas = camisetaRepository.findAll();
        model.addAttribute("camisetas", camisetas);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "cliente/camiseta/list";
    }

    @GetMapping("/categorias/list")
    public String listCategorias(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "cliente/categoria/list";
    }

    @PostMapping("/carrito/add/{id}")
    public String addToCart(@PathVariable("id") Integer id, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");

        if (carrito == null)
            carrito = new HashMap<>();
        
        carrito.put(id, carrito.getOrDefault(id, 0) + 1);
        session.setAttribute("carrito", carrito);
        return "redirect:/cliente/camisetas/list";
    }

    @GetMapping("/carrito")
    public String viewCart(Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> carritoIDs = (Map<Integer, Integer>) session.getAttribute("carrito");
        
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal totalGlobal = BigDecimal.ZERO;

        if (carritoIDs != null && !carritoIDs.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : carritoIDs.entrySet()) {
                Optional<Camiseta> oCamiseta = camisetaRepository.findById(entry.getKey());
                if (oCamiseta.isPresent()) {
                    Camiseta camiseta = oCamiseta.get();
                    int cantidad = entry.getValue();
                    
                    // Cálculo de subtotal con BigDecimal
                    BigDecimal cantidadBD = new BigDecimal(cantidad);
                    BigDecimal subtotalBD = camiseta.getPrecio().multiply(cantidadBD); 
                    totalGlobal = totalGlobal.add(subtotalBD);

                    Map<String, Object> item = new HashMap<>();
                    item.put("camiseta", camiseta);
                    item.put("cantidad", cantidad);
                    item.put("subtotal", subtotalBD.doubleValue());
                    items.add(item);
                }
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("totalGlobal", totalGlobal.doubleValue());
        
        return "cliente/carrito/view";
    }

    @PostMapping("/carrito/update")
    public String updateCart(@RequestParam("id") Integer id, @RequestParam("cantidad") Integer cantidad, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");

        if (carrito != null) {
            if (cantidad <= 0)
                carrito.remove(id);
            else
                carrito.put(id, cantidad);
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/cliente/carrito";
    }
    
    @PostMapping("/carrito/buy")
    @Transactional
    public String buyCart(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> carritoIDs = (Map<Integer, Integer>) session.getAttribute("carrito");
        
        if (carritoIDs != null && !carritoIDs.isEmpty()) {
            
            for (Map.Entry<Integer, Integer> entry : carritoIDs.entrySet()) {
                
                Integer camisetaId = entry.getKey();
                Integer cantidadComprada = entry.getValue();
                
                Optional<Camiseta> oCamiseta = camisetaRepository.findById(camisetaId);
                
                if (oCamiseta.isPresent()) {
                    Camiseta camiseta = oCamiseta.get();
                    // Lógica para actualizar el stock:
                    int stockActual = camiseta.getStock();
                    int nuevoStock = stockActual - cantidadComprada;
                    
                    if (nuevoStock >= 0) {
                        camiseta.setStock(nuevoStock);
                        camisetaRepository.save(camiseta);
                    } else
                        System.err.println("Advertencia: Stock insuficiente para Camiseta ID: " + camisetaId);
                }
            }
        }
        session.removeAttribute("carrito");
        model.addAttribute("mensaje", "¡Compra realizada con éxito!");
        return "redirect:/cliente/camisetas/list"; 
    }
}