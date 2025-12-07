package com.example.proyectoCamisetas.repository;

import com.example.proyectoCamisetas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    /**
     * Método clave para la autenticación: busca un usuario por su nombre de usuario.
     */
    Optional<Usuario> findByUsername(String username);
}