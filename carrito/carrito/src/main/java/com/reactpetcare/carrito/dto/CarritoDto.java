package com.reactpetcare.carrito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "CarritoDto",
    description = "Representa el carrito de compras completo asociado a un usuario"
)
public class CarritoDto {

    @Schema(
        description = "ID del usuario propietario del carrito",
        example = "7",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    @Schema(
        description = "Lista de ítems agregados al carrito. Puede estar vacía si el usuario no ha agregado productos",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<CarritoItemDto> items;
}
