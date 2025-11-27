package com.reactpetcare.usuario.dto;

import com.reactpetcare.usuario.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con datos del usuario")
public class UsuarioDto {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Francisca Castro")
    private String nombre;

    @Schema(description = "Correo electrónico", example = "francisca@example.com")
    private String email;

    @Schema(description = "Número telefónico", example = "+56912345678")
    private String telefono;

    @Schema(description = "Rol asignado", example = "CLIENTE")
    private RolUsuario rol;
}
