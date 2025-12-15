package com.reactpetcare.pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detalle de un producto dentro de un pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del detalle del pedido",
        example = "100",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false)
    @Schema(
        description = "ID del producto en el microservicio de productos",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    @Column(nullable = false)
    @Schema(
        description = "Nombre del producto al momento de la compra",
        example = "Shampoo para perros",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombreProducto;

    @Column(nullable = false)
    @Schema(
        description = "Cantidad comprada del producto",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(
        description = "Precio unitario del producto al momento del pedido",
        example = "7990",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double precioUnitario;

    @Column(nullable = false)
    @Schema(
        description = "Subtotal del detalle (precioUnitario × cantidad)",
        example = "15980",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @Schema(
        description = "Pedido al que pertenece este detalle",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Pedido pedido;
}
