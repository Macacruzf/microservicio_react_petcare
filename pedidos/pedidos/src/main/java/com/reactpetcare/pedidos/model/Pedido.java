package com.reactpetcare.pedidos.model;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Pedido realizado por un usuario")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del pedido",
        example = "10",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "ID del usuario que realiza el pedido",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    @Schema(
        description = "Nombre del usuario que realiza el pedido",
        example = "Francisca Castro"
    )
    private String nombreUsuario;

    @Schema(
        description = "Correo electrónico del usuario que realiza el pedido",
        example = "francisca@example.com"
    )
    private String emailUsuario;

    @Schema(
        description = "Fecha y hora de creación del pedido",
        example = "2025-01-10T14:30:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Schema(
        description = "Estado actual del pedido",
        example = "PENDIENTE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @Schema(
        description = "Lista de detalles asociados al pedido"
    )
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<DetallePedido> detalles;

    @Schema(
        description = "Monto total del pedido",
        example = "25990",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Double total;
}
