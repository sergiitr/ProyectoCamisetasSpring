// src/main/java/com/example/proyectoCamisetas/controller/AuthController.java
package com.example.proyectoCamisetas.controller;

import com.example.proyectoCamisetas.entity.Usuario;
import com.example.proyectoCamisetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra la página de inicio de sesión (Login).
     */
    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login"; 
    }

    /**
     * Muestra el formulario para registrar un nuevo usuario.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/register";
    }

    /**
     * Procesa la creación de un nuevo usuario (Registro).
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute Usuario usuario) {
        
        // 1. CIFRAR la contraseña
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);

        // 2. Asignar rol por defecto y estado activo
        usuario.setTipo(Usuario.Tipo.CLIENTE); 
        usuario.setActivo(true); 
        
        // 3. Guardar el nuevo usuario en la BBDD
        usuarioRepository.save(usuario);

        // 4. Redirigir al login
        return "redirect:/login?registered=true";
    }
}