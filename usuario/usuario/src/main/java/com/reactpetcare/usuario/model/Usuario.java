package com.reactpetcare.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del usuario",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false)
    @Schema(
        description = "Nombre del usuario",
        example = "Francisca",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Column(nullable = false, unique = true)
    @Schema(
        description = "Nombre de usuario único para login",
        example = "francisca.castro",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Column(nullable = false)
    @Schema(
        description = "Apellido del usuario",
        example = "Castro",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String apellido;

    @Column(nullable = false)
    @Schema(
        description = "Dirección del usuario",
        example = "Av. Principal 123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String direccion;

    @Column(nullable = false, unique = true)
    @Schema(
        description = "Correo electrónico único del usuario",
        example = "francisca@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
        description = "Número telefónico del usuario",
        example = "+56912345678"
    )
    private String telefono;

    @Column(nullable = false)
    @Schema(
        description = "Contraseña del usuario (solo para creación o actualización)",
        example = "********",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Schema(description = "Roles asignados al usuario")
    private Set<Rol> roles = new HashSet<>();
}
