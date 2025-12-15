package com.reactpetcare.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta de login con datos del usuario (sin JWT)")
public class LoginResponse {

    @Schema(description = "ID del usuario", example = "1")
    private Long userId;

    @Schema(description = "Nombre del usuario", example = "Francisca")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "González")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "francisca@example.com")
    private String email;

    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private String rol;

    @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}
