package com.reactpetcare.carrito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor        // constructor con usuarioId + items
@NoArgsConstructor 
@Schema(description = "Respuesta del microservicio de productos usada por el carrito")
public class ProductoResponse {

    @Schema(description = "ID del producto", example = "10")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Shampoo para perros")
    private String nombre;

    @Schema(description = "Precio unitario del producto", example = "7990")
    private Double precio;

    @Schema(description = "Stock disponible", example = "5")
    private Integer stock;

    @Schema(description = "Estado del producto (DISPONIBLE o SIN_STOCK)", example = "DISPONIBLE")
    private String estado;
}
