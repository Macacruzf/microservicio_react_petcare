package com.reactpetcare.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Petición de registro de usuario")
public class RegistroRequest {

    @Schema(description = "Nombre del usuario", example = "Francisca")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Castro")
    private String apellido;

    @Schema(description = "Nombre de usuario", example = "francisca.castro")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "francisca@example.com")
    private String email;

    @Schema(description = "Dirección del usuario", example = "Av. Principal 123")
    private String direccion;

    @Schema(description = "Teléfono", example = "+56912345678")
    private String telefono;

    @Schema(description = "Contraseña del usuario", example = "Password123")
    private String password;

    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private String rol;
}
