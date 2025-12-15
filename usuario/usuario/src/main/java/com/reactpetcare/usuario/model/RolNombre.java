package com.reactpetcare.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Roles disponibles dentro del sistema",
    example = "ADMIN"
)
public enum RolNombre {

    @Schema(description = "Rol administrador del sistema")
    ADMIN,

    @Schema(description = "Rol cliente con permisos básicos")
    CLIENTE
}
