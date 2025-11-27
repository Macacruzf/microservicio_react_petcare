package com.reactpetcare.usuario.dto;

import com.reactpetcare.usuario.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta de login con token JWT y datos del usuario")
public class LoginResponse {

    @Schema(description = "Token JWT de acceso")
    private String token;

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Francisca")
    private String nombre;

    @Schema(description = "Correo electrónico", example = "francisca@example.com")
    private String email;

    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private RolUsuario rol;
}
