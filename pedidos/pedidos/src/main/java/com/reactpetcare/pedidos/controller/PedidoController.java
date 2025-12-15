package com.reactpetcare.pedidos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reactpetcare.pedidos.model.EstadoPedido;
import com.reactpetcare.pedidos.model.Pedido;
import com.reactpetcare.pedidos.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
@Tag(
    name = "Pedidos",
    description = "Microservicio para gestión de pedidos y ventas"
)
public class PedidoController {

    private final PedidoService pedidoService;

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Crear un pedido desde el carrito del usuario",
        description = "Genera un pedido a partir del carrito asociado a un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Pedido creado correctamente",
            content = @Content(
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Carrito vacío o datos inválidos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/crear/{usuarioId}")
    public ResponseEntity<Pedido> crearPedido(
            @Parameter(
                description = "ID del usuario al que pertenece el carrito",
                example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        Pedido pedido = pedidoService.crearPedido(usuarioId);
        return ResponseEntity.ok(pedido);
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Obtener un pedido por su ID",
        description = "Obtiene el detalle completo de un pedido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado",
            content = @Content(
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(
            @Parameter(
                description = "ID del pedido",
                example = "10"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Listar todos los pedidos",
        description = "Obtiene el listado completo de pedidos registrados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen pedidos registrados"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Listar pedidos por usuario",
        description = "Obtiene los pedidos realizados por un usuario específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Pedidos del usuario obtenidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(
            @Parameter(
                description = "ID del usuario",
                example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Cambiar estado de un pedido",
        description = "Actualiza el estado de un pedido según el flujo de venta"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado del pedido actualizado",
            content = @Content(
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
            @Parameter(
                description = "ID del pedido",
                example = "10"
            )
            @PathVariable Long id,

            @Parameter(
                description = "Nuevo estado del pedido",
                schema = @Schema(implementation = EstadoPedido.class),
                example = "ENTREGADO"
            )
            @RequestParam EstadoPedido estado
    ) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }
}
