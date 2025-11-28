package com.reactpetcare.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor        // constructor con usuarioId + items
@NoArgsConstructor 
@Schema(description = "Producto incluido en el pedido")
public class DetallePedidoRequest {

    @Schema(description = "ID del producto a comprar", example = "3")
    private Long productoId;

    @Schema(description = "Cantidad del producto", example = "2")
    private Integer cantidad;
}
