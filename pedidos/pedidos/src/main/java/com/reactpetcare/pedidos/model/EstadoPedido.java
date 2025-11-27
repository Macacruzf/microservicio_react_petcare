package com.reactpetcare.pedidos.model;

public enum EstadoPedido {
    PENDIENTE,      // El pedido se creó (aún no está listo)
    POR_ENTREGAR,   // La tienda preparó el pedido
    ENTREGADO,      // El usuario retiró su compra
    CANCELADO       // (Opcional) Pedido anulado
}
