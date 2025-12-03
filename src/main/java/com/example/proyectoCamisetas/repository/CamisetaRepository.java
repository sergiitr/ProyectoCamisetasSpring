package com.example.proyectoCamisetas.repository;

import com.example.proyectoCamisetas.entity.Camiseta;
import com.example.proyectoCamisetas.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CamisetaRepository extends JpaRepository<Camiseta, Integer> {
    List<Camiseta> findByCategoria(Categoria categoria);
}