package com.reactpetcare.carrito.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "Carrito",
    description = "Representa el carrito de compras asociado a un usuario"
)
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del carrito",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "ID del usuario dueño del carrito",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    @OneToMany(
        mappedBy = "carrito",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    @Schema(
        description = "Lista de ítems del carrito (relación interna, no expuesta por la API)",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<CarritoItem> items = new ArrayList<>();
}
