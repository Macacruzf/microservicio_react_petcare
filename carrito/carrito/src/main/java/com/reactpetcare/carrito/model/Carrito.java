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
@Schema(description = "Entidad que representa el carrito de compras del usuario")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del carrito", example = "1")
    private Long id;

    @Schema(description = "ID del usuario dueño del carrito", example = "10")
    private Long usuarioId;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  
    @Schema(description = "Lista interna de ítems del carrito (relación)")
    private List<CarritoItem> items = new ArrayList<>();
}
