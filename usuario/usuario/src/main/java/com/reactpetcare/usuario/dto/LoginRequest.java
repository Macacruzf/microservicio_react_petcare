package com.reactpetcare.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Petición de inicio de sesión")
public class LoginRequest {

    @Schema(description = "Correo electrónico", example = "francisca@example.com")
    private String email;

    @Schema(description = "Contraseña", example = "Password123")
    private String password;
}
