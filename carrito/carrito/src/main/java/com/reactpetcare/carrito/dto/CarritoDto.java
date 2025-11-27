package com.reactpetcare.carrito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Carrito completo del usuario")
public class CarritoDto {

    @Schema(description = "ID del usuario dueño del carrito", example = "7")
    private Long usuarioId;

    @Schema(description = "Lista de productos agregados al carrito")
    private List<CarritoItemDto> items;
}
