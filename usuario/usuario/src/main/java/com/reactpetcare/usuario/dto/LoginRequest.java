package com.reactpetcare.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    description = "Petición de inicio de sesión. Contiene las credenciales del usuario."
)
public class LoginRequest {

    @Schema(
        description = "Correo electrónico del usuario",
        example = "francisca@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
        description = "Contraseña del usuario",
        example = "********",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;
}
