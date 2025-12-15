package com.reactpetcare.pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Estado del pedido dentro del flujo de compra",
    example = "PENDIENTE"
)
public enum EstadoPedido {

    @Schema(description = "Pedido creado, aún no preparado ni entregado")
    PENDIENTE,

    @Schema(description = "Pedido preparado por la tienda y listo para entrega")
    POR_ENTREGAR,

    @Schema(description = "Pedido entregado al usuario")
    ENTREGADO,

    @Schema(description = "Pedido cancelado antes de la entrega")
    CANCELADO
}
