package com.reactpetcare.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    description = "DTO de usuario. Uso académico. " +
                  "El campo password se utiliza solo para registro o modificación"
)
public class UsuarioDto {

    @Schema(
        description = "ID del usuario",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del usuario",
        example = "Francisca"
    )
    private String nombre;

    @Schema(
        description = "Apellido del usuario",
        example = "Castro"
    )
    private String apellido;

    @Schema(
        description = "Correo electrónico",
        example = "francisca@example.com"
    )
    private String email;

    @Schema(
        description = "Dirección del usuario",
        example = "Av. Principal 123"
    )
    private String direccion;

    @Schema(
        description = "Número telefónico",
        example = "+56912345678"
    )
    private String telefono;

    @Schema(
        description = "Nombre de usuario",
        example = "francisca.castro"
    )
    private String username;

    @Schema(
        description = "Contraseña del usuario (solo para registro o modificación)",
        example = "password123",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @Schema(
        description = "Rol asignado al usuario",
        example = "CLIENTE"
    )
    private String rol;
}
