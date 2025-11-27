package com.reactpetcare.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reactpetcare.productos.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
