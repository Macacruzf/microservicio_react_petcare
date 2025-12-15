package com.reactpetcare.productos.dto;

import com.reactpetcare.productos.model.EstadoProducto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para crear o actualizar un producto")
public class ProductoDto {

    @Schema(
        description = "ID del producto",
        example = "10",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del producto",
        example = "Shampoo para perros",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
        description = "Precio del producto",
        example = "5990",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double precio;

    @Schema(
        description = "Descripción del producto",
        example = "Shampoo para piel sensible"
    )
    private String descripcion;

    @Schema(
        description = "ID de la categoría asociada al producto",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long categoriaId;

    @Schema(
        description = "Stock disponible del producto",
        example = "15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer stock;

    @Schema(
        description = "Estado actual del producto",
        example = "DISPONIBLE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EstadoProducto estado;

    @Schema(
        description = "Imagen del producto (URL o Base64 según implementación)",
        example = "https://example.com/image.jpg"
    )
    private String imagen;
}
