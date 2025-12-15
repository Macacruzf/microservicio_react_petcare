package com.reactpetcare.productos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Producto disponible en la tienda")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del producto",
        example = "10",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false)
    @Schema(
        description = "Nombre del producto",
        example = "Collar para perro",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Column(nullable = false)
    @Schema(
        description = "Precio del producto en pesos chilenos",
        example = "8990",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double precio;

    @Schema(
        description = "Descripción detallada del producto",
        example = "Collar ajustable color rojo"
    )
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @Schema(
        description = "Categoría a la que pertenece el producto",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Categoria categoria;

    @Column(nullable = false)
    @Schema(
        description = "Cantidad de unidades disponibles en stock",
        example = "30",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
        description = "Estado del producto según disponibilidad de stock",
        example = "DISPONIBLE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EstadoProducto estado;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Schema(
        description = "Imagen del producto codificada en Base64",
        example = "data:image/png;base64,iVBORw0KGgo...",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String imagen;
}
