package com.reactpetcare.productos.controller;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.Producto;
import com.reactpetcare.productos.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Microservicio para gestión de productos y categorías")
public class ProductoController {

    private final ProductoService productoService;

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Obtener un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Crear producto")
    @PostMapping
    public ResponseEntity<ProductoDto> crear(@RequestBody ProductoDto dto) {
        return ResponseEntity.ok(productoService.crear(dto));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(@PathVariable Long id, @RequestBody ProductoDto dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Descontar stock — usado por Pedidos")
    @PostMapping("/descontar/{id}")
    public ResponseEntity<Void> descontarStock(
            @PathVariable Long id,
            @RequestParam int cantidad
    ) {
        productoService.descontarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }
}
