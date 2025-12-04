package com.example.proyectoCamisetas.repository;

import com.example.proyectoCamisetas.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {}
