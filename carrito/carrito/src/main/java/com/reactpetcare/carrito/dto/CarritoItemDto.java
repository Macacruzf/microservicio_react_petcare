package com.reactpetcare.carrito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "CarritoItemDto",
    description = "Representa un ítem agregado al carrito de compras por el usuario"
)
public class CarritoItemDto {

    @Schema(
        description = "ID del producto agregado al carrito (referencia al microservicio de productos)",
        example = "12",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    @Schema(
        description = "Cantidad del producto agregada al carrito",
        example = "3",
        minimum = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidad;
}
