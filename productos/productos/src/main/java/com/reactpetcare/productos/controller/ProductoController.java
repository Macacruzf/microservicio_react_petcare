package com.reactpetcare.productos.controller;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.Producto;
import com.reactpetcare.productos.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
@Tag(
    name = "Productos",
    description = "Microservicio para gestión de productos y categorías"
)
public class ProductoController {

    private final ProductoService productoService;

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene el listado completo de productos disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen productos registrados"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Obtener un producto por ID",
        description = "Obtiene los datos de un producto específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Crear producto",
        description = "Crea un nuevo producto en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado correctamente",
            content = @Content(
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Producto duplicado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping
    public ResponseEntity<ProductoDto> crear(@RequestBody ProductoDto dto) {
        return ResponseEntity.ok(productoService.crear(dto));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza los datos de un producto existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado correctamente",
            content = @Content(
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoDto dto
    ) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Producto eliminado correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Descontar stock de un producto",
        description = "Descuenta stock del producto. Usado por el microservicio de pedidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock descontado correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Cantidad inválida"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Stock insuficiente"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/{id}/descontar")
    public ResponseEntity<Void> descontarStock(
            @PathVariable Long id,
            @RequestParam int cantidad
    ) {
        productoService.descontarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }
}
