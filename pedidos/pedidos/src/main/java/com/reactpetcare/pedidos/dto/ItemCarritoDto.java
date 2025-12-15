package com.reactpetcare.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item dentro del carrito del usuario")
public class ItemCarritoDto {

    @Schema(description = "ID del producto", example = "5")
    private Long productoId;

    @Schema(description = "Cantidad agregada al carrito", example = "2")
    private Integer cantidad;
}
