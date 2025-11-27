package com.reactpetcare.carrito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item dentro del carrito del usuario")
public class CarritoItemDto {

    @Schema(description = "ID del producto", example = "12")
    private Long productoId;

    @Schema(description = "Cantidad agregada", example = "3")
    private Integer cantidad;
}
