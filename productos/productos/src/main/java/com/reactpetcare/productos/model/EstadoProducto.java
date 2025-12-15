package com.reactpetcare.productos.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Estado del producto según su disponibilidad en stock",
    example = "DISPONIBLE"
)
public enum EstadoProducto {

    @Schema(description = "Producto con stock disponible para la venta")
    DISPONIBLE,

    @Schema(description = "Producto sin unidades disponibles en stock")
    SIN_STOCK
}
