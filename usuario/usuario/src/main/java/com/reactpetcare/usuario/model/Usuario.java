package com.reactpetcare.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del usuario",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false)
    @Schema(
        description = "Nombre completo del usuario",
        example = "Francisca Castro"
    )
    private String nombre;

    @Column(nullable = false, unique = true)
    @Schema(
        description = "Correo electrónico único del usuario",
        example = "francisca@example.com"
    )
    private String email;

    @Schema(
        description = "Número telefónico del usuario",
        example = "+56912345678"
    )
    private String telefono;

    @Column(nullable = false)
    @Schema(
        description = "Contraseña encriptada del usuario. SOLO escritura.",
        example = "MiPassword123",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
        description = "Rol del usuario dentro del sistema",
        example = "CLIENTE",
        allowableValues = {"CLIENTE", "ADMIN"}
    )
    private RolUsuario rol;
}
