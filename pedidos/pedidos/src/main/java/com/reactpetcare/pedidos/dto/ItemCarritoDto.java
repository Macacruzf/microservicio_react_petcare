package com.reactpetcare.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Item dentro del carrito del usuario")
public class ItemCarritoDto {

    @Schema(description = "ID del producto", example = "5")
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Shampoo antipulgas")
    private String nombre;

    @Schema(description = "Precio del producto", example = "7990")
    private Double precio;

    @Schema(description = "Cantidad agregada al carrito", example = "2")
    private Integer cantidad;
}
