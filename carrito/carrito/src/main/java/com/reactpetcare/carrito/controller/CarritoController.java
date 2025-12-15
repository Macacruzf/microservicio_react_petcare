package com.reactpetcare.carrito.controller;

import com.reactpetcare.carrito.dto.CarritoDto;
import com.reactpetcare.carrito.dto.CarritoItemDto;
import com.reactpetcare.carrito.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
@Tag(
    name = "Carrito",
    description = "Microservicio que gestiona el carrito de compras del usuario"
)
public class CarritoController {

    private final CarritoService carritoService;

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Obtener carrito del usuario",
        description = "Retorna el carrito completo asociado al usuario indicado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carrito encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrito no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoDto> obtener(
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Agregar un producto al carrito",
        description = "Agrega un producto al carrito del usuario. Si el carrito no existe, se crea automáticamente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto agregado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoDto.class)
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
    @PostMapping("/{usuarioId}/agregar")
    public ResponseEntity<CarritoDto> agregar(
            @PathVariable Long usuarioId,

            @RequestBody(
                description = "Datos del producto que se agregará al carrito",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CarritoItemDto.class)
                )
            )
            @org.springframework.web.bind.annotation.RequestBody
            CarritoItemDto itemDto
    ) {
        return ResponseEntity.ok(
                carritoService.agregarProducto(usuarioId, itemDto)
        );
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Vaciar carrito del usuario",
        description = "Elimina todos los productos asociados al carrito del usuario"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carrito vaciado correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrito no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciar(
            @PathVariable Long usuarioId
    ) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.ok().build();
    }
}
