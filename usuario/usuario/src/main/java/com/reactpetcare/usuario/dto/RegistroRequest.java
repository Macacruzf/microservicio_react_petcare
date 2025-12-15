package com.reactpetcare.usuario.dto;

import com.reactpetcare.usuario.model.RolNombre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    description = "Petición de registro de un nuevo usuario en el sistema",
    accessMode = Schema.AccessMode.WRITE_ONLY
)
public class RegistroRequest {

    @Schema(
        description = "Nombre del usuario",
        example = "Francisca",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
        description = "Apellido del usuario",
        example = "Castro",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String apellido;

    @Schema(
        description = "Nombre de usuario único",
        example = "francisca.castro",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Schema(
        description = "Correo electrónico del usuario",
        example = "francisca@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
        description = "Dirección del usuario",
        example = "Av. Principal 123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String direccion;

    @Schema(
        description = "Número telefónico del usuario",
        example = "+56912345678"
    )
    private String telefono;

    @Schema(
        description = "Contraseña del usuario",
        example = "********",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @Schema(
        description = "Rol asignado al usuario",
        implementation = RolNombre.class,
        example = "CLIENTE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private RolNombre rol;
}
