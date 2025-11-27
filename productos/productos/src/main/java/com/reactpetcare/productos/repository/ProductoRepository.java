package com.reactpetcare.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reactpetcare.productos.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}
