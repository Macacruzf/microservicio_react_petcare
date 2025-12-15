package com.reactpetcare.productos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Producto disponible en la tienda")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del producto", example = "10")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Collar para perro")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Precio del producto", example = "8990")
    private Double precio;

    @Schema(description = "Descripción del producto", example = "Collar ajustable color rojo")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @Schema(description = "Categoría del producto")
    private Categoria categoria;

    @Column(nullable = false)
    @Schema(description = "Stock disponible del producto", example = "30")
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
            description = "Estado del producto según stock",
            example = "DISPONIBLE"
    )
    private EstadoProducto estado;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Schema(description = "Imagen en formato Base64", example = "data:image/png;base64,iVBORw0KGgo...")
    private String imagen;
}
