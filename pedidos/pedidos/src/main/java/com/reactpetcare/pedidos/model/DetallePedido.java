package com.reactpetcare.pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Schema(description = "ID del detalle de pedido", example = "100")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID del producto en el microservicio de productos", example = "5")
    private Long productoId;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto al momento de la compra", example = "Shampoo para perros")
    private String nombreProducto;

    @Column(nullable = false)
    @Schema(description = "Cantidad comprada", example = "2")
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Precio unitario al momento del pedido", example = "7990")
    private Double precioUnitario;

    @Column(nullable = false)
    @Schema(description = "Subtotal (precioUnitario * cantidad)", example = "15980")
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @Schema(description = "Pedido al que pertenece este detalle")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Pedido pedido;
}
