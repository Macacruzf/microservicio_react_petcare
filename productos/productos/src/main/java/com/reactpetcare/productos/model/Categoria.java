package com.reactpetcare.productos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Categoría de un producto")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único de la categoría",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(
        description = "Nombre de la categoría",
        example = "Accesorios",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;
}
