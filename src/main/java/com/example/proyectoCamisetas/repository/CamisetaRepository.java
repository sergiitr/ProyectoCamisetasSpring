package com.example.proyectoCamisetas.repository;

import com.example.proyectoCamisetas.entity.Camiseta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamisetaRepository extends JpaRepository<Camiseta, Integer> {}
