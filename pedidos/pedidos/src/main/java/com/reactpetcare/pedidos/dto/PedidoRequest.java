package com.reactpetcare.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor        // constructor con usuarioId + items
@NoArgsConstructor 
@Schema(description = "Datos para crear un pedido")
public class PedidoRequest {

    @Schema(description = "ID del usuario que realiza el pedido", example = "1")
    private Long usuarioId;

    @Schema(description = "Lista de productos a comprar")
    private List<DetallePedidoRequest> productos;
}
