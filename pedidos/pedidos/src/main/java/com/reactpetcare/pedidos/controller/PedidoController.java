package com.reactpetcare.pedidos.controller;

import com.reactpetcare.pedidos.model.EstadoPedido;
import com.reactpetcare.pedidos.model.Pedido;
import com.reactpetcare.pedidos.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Microservicio para gestión de pedidos y ventas")
public class PedidoController {

    private final PedidoService pedidoService;

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Crear un pedido desde el carrito del usuario")
    @PostMapping("/crear/{usuarioId}")
    public ResponseEntity<Pedido> crearPedido(
            @Parameter(description = "ID del usuario al que pertenece el carrito")
            @PathVariable Long usuarioId
    ) {
        Pedido pedido = pedidoService.crearPedido(usuarioId);
        return ResponseEntity.ok(pedido);
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Obtener un pedido por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(
            @Parameter(description = "ID del pedido")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Listar todos los pedidos")
    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Listar pedidos realizados por un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(
            @Parameter(description = "ID del usuario")
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    // ────────────────────────────────────────────────────────────────
    @Operation(summary = "Cambiar estado de un pedido")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
            @Parameter(description = "ID del pedido")
            @PathVariable Long id,

            @Parameter(description = "Nuevo estado del pedido: PENDIENTE, POR_ENTREGAR, ENTREGADO")
            @RequestParam EstadoPedido estado
    ) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }
}
