package com.reactpetcare.carrito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "ProductoResponse",
    description = "Representación del producto obtenida desde el microservicio de productos, usada por el carrito"
)
public class ProductoResponse {

    @Schema(
        description = "ID único del producto",
        example = "10",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del producto",
        example = "Shampoo para perros"
    )
    private String nombre;

    @Schema(
        description = "Precio unitario del producto",
        example = "7990"
    )
    private Double precio;

    @Schema(
        description = "Stock disponible del producto",
        example = "5"
    )
    private Integer stock;

    @Schema(
        description = "Estado actual del producto según stock",
        example = "DISPONIBLE",
        allowableValues = {"DISPONIBLE", "SIN_STOCK"}
    )
    private String estado;
}
