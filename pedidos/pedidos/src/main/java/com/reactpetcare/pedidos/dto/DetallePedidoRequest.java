package com.reactpetcare.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Producto incluido en el pedido")
public class DetallePedidoRequest {

    @Schema(description = "ID del producto a comprar", example = "3")
    private Long productoId;

    @Schema(description = "Cantidad del producto", example = "2")
    private Integer cantidad;
}
