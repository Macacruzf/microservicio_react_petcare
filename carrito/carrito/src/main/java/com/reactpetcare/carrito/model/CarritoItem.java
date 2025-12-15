package com.reactpetcare.carrito.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "carrito_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "CarritoItem",
    description = "Representa un ítem individual dentro del carrito de compras del usuario"
)
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del ítem dentro del carrito",
        example = "5",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "ID del producto agregado al carrito (referencia al microservicio de productos)",
        example = "12",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    @Schema(
        description = "Cantidad seleccionada del producto",
        example = "3",
        minimum = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    @JsonIgnore
    @Schema(
        description = "Carrito al que pertenece este ítem (relación interna, no expuesta en la API)",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Carrito carrito;
}
