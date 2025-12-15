package com.reactpetcare.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Carrito completo del usuario")
public class CarritoDto {

    @Schema(description = "ID del usuario propietario del carrito", example = "1")
    private Long usuarioId;

    @Schema(description = "Items del carrito")
    private List<ItemCarritoDto> items;

    @Schema(description = "Total del carrito", example = "19980")
    private Double total;
}
