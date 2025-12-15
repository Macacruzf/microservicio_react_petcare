package com.reactpetcare.productos.service;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.*;
import com.reactpetcare.productos.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .imagen(dto.getImagen())
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
        producto.setImagen(dto.getImagen());

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
    @Transactional
    public void descontarStock(Long id, int cantidad) {

        System.out.println("🔻 Recibida solicitud de descuento de stock - Producto ID: " + id + " | Cantidad: " + cantidad);
        
        Producto producto = obtenerPorId(id);
        
        System.out.println("📦 Producto: " + producto.getNombre() + " | Stock actual: " + producto.getStock());

        if (producto.getStock() <= 0) {
            System.err.println("❌ Error: Producto sin stock disponible");
            throw new RuntimeException("Producto sin stock disponible");
        }

        if (producto.getStock() < cantidad) {
            System.err.println("❌ Error: Stock insuficiente. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
            throw new RuntimeException("Stock insuficiente para completar la compra");
        }

        // Descontar el stock
        int stockAnterior = producto.getStock();
        producto.setStock(producto.getStock() - cantidad);
        int stockNuevo = producto.getStock();

        // Cambiar el estado si queda en 0
        if (producto.getStock() == 0) {
            producto.setEstado(EstadoProducto.SIN_STOCK);
            System.out.println("⚠️ Producto quedó sin stock, cambiando estado a SIN_STOCK");
        }

        productoRepo.save(producto);
        System.out.println("✅ Stock descontado exitosamente: " + stockAnterior + " → " + stockNuevo);
    }
}
