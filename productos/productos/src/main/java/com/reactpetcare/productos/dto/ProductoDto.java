package com.reactpetcare.productos.dto;

import com.reactpetcare.productos.model.EstadoProducto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para crear o actualizar un producto")
public class ProductoDto {

    private Long id;

    @Schema(description = "Nombre del producto", example = "Shampoo para perros")
    private String nombre;

    @Schema(description = "Precio", example = "5990")
    private Double precio;

    @Schema(description = "Descripción", example = "Shampoo para piel sensible")
    private String descripcion;

    @Schema(description = "ID de la categoría", example = "1")
    private Long categoriaId;

    @Schema(description = "Stock del producto", example = "15")
    private Integer stock;

    @Schema(description = "Estado del producto", example = "DISPONIBLE")
    private EstadoProducto estado;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/image.jpg")
    private String imagen;
}
