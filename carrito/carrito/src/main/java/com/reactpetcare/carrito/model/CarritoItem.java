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
@Schema(description = "Ítem dentro del carrito de compras")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del ítem dentro del carrito", example = "5")
    private Long id;

    @Schema(description = "ID del producto agregado al carrito", example = "12")
    private Long productoId;

    @Schema(description = "Cantidad seleccionada del producto", example = "3")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    @JsonIgnore  // ⛔ evita recursión e infinito bucle en JSON y Swagger
    @Schema(description = "Carrito al que pertenece este ítem (relación interna)")
    private Carrito carrito;
}
