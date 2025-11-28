package com.reactpetcare.pedidos.model;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Schema(description = "ID del pedido", example = "10")
    private Long id;

    @Schema(description = "ID del usuario que realiza el pedido", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre del usuario que realiza el pedido", example = "Francisca Castro")
    private String nombreUsuario;

    @Schema(description = "Correo del usuario que realiza el pedido", example = "francisca@example.com")
    private String emailUsuario;

    @Schema(description = "Fecha de creación del pedido")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado del pedido", example = "PENDIENTE")
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @Schema(description = "Detalles asociados al pedido")
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<DetallePedido> detalles;

    @Schema(description = "Total del pedido", example = "25990")
    private Double total;
}
