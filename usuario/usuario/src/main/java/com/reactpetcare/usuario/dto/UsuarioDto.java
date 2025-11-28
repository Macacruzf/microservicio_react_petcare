package com.reactpetcare.usuario.dto;

import com.reactpetcare.usuario.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con datos del usuario")
public class UsuarioDto {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Francisca")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Castro")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "francisca@example.com")
    private String email;

    @Schema(description = "Dirección del usuario", example = "Av. Principal 123")
    private String direccion;

    @Schema(description = "Número telefónico", example = "+56912345678")
    private String telefono;

    @Schema(description = "Contraseña (solo para registro)", example = "password123")
    private String password;

    @Schema(description = "Rol asignado", example = "CLIENTE")
    private String rol;
}
