package com.reactpetcare.productos.service;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.*;
import com.reactpetcare.productos.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    // =====================================================
    // CREAR PRODUCTO
    // =====================================================
    public ProductoDto crear(ProductoDto dto) {

        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .stock(dto.getStock())
                .estado(dto.getStock() > 0 ? EstadoProducto.DISPONIBLE : EstadoProducto.SIN_STOCK)
                .build();

        producto = productoRepo.save(producto);

        dto.setId(producto.getId());
        dto.setEstado(producto.getEstado());
        return dto;
    }

    // =====================================================
    // LISTAR PRODUCTOS
    // =====================================================
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    public Producto obtenerPorId(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // =====================================================
    // ACTUALIZAR PRODUCTO
    // =====================================================
    public ProductoDto actualizar(Long id, ProductoDto dto) {

        Producto producto = obtenerPorId(id);
        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(categoria);
        producto.setStock(dto.getStock());

        // Actualizar estado
        producto.setEstado(dto.getStock() > 0 ? EstadoProducto.DISPONIBLE : EstadoProducto.SIN_STOCK);

        productoRepo.save(producto);

        dto.setId(producto.getId());
        dto.setEstado(producto.getEstado());
        return dto;
    }

    // =====================================================
    // ELIMINAR PRODUCTO
    // =====================================================
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepo.delete(producto);
    }

    // =====================================================
    // DESCONTAR STOCK (USADO POR PEDIDOS)
    // =====================================================
    public void descontarStock(Long id, int cantidad) {

        Producto producto = obtenerPorId(id);

        if (producto.getStock() <= 0) {
            throw new RuntimeException("Producto sin stock disponible");
        }

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para completar la compra");
        }

        // Descontar el stock
        producto.setStock(producto.getStock() - cantidad);

        // Cambiar el estado si queda en 0
        if (producto.getStock() == 0) {
            producto.setEstado(EstadoProducto.SIN_STOCK);
        }

        productoRepo.save(producto);
    }
}
