package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.entity.Categoria;
import com.example.proyectoCamisetas.repository.CamisetaRepository;
import com.example.proyectoCamisetas.repository.CategoriaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Muestra la página de inicio/índice del cliente.
     * Mapea: GET /cliente o GET /cliente/
     * * NOTA: Este es el punto de entrada para usuarios con rol CLIENTE.
     * @return Nombre de la plantilla index.html del cliente.
     */
    @GetMapping({"", "/"})
    public String clienteHome() {
        return "cliente/index"; 
    }

    /**
     * Muestra el listado de todas las camisetas para el cliente.
     * Mapea: GET /cliente/camisetas/list
     */
    @GetMapping("/camisetas/list")
    public String listCamisetas(Model model) {
        List<Camiseta> camisetas = camisetaRepository.findAll();
        model.addAttribute("camisetas", camisetas);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "cliente/camiseta/list";
    }

    /**
     * Muestra el listado de todas las categorías disponibles.
     * Mapea: GET /cliente/categorias/list
     */
    @GetMapping("/categorias/list")
    public String listCategorias(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "cliente/categoria/list";
    }

    /**
     * Añade una camiseta al carrito de compras (almacenado en la sesión).
     * Mapea: POST /cliente/carrito/add/{id}
     */
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

    /**
     * Muestra el contenido actual del carrito.
     * Mapea: GET /cliente/carrito
     */
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
                    
                    BigDecimal cantidadBD = new BigDecimal(cantidad);
                    BigDecimal subtotalBD = camiseta.getPrecio().multiply(cantidadBD); 
                    totalGlobal = totalGlobal.add(subtotalBD);

                    Map<String, Object> item = new HashMap<>();
                    item.put("camiseta", camiseta);
                    item.put("cantidad", cantidad);

                    double subtotal = subtotalBD.doubleValue(); 
                    
                    item.put("subtotal", subtotal);
                    items.add(item);
                }
            }
        }
        
        model.addAttribute("items", items);
        model.addAttribute("totalGlobal", totalGlobal.doubleValue()); 
        
        return "cliente/carrito/view";
    }

    /**
     * Actualiza la cantidad de un producto en el carrito o lo elimina si la cantidad es cero.
     * Mapea: POST /cliente/carrito/update
     */
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
    
    /**
     * Procesa la compra (simulada) y vacía el carrito.
     * Mapea: POST /cliente/carrito/buy
     */
    @PostMapping("/carrito/buy")
    public String buyCart(HttpSession session, Model model) {
        session.removeAttribute("carrito");        
        model.addAttribute("mensaje", "¡Compra realizada con éxito! Su carrito ha sido vaciado.");
        return "redirect:/cliente/camisetas/list"; 
    }
}