package com.reactpetcare.usuario.controller;

import com.reactpetcare.usuario.dto.*;
import com.reactpetcare.usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Usuarios", description = "API de gestión interna de usuarios con autenticación JWT")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ---------------------------------------------------------
    // REGISTRO
    // ---------------------------------------------------------
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea un usuario con rol CLIENTE por defecto y devuelve sus datos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado correctamente",
            content = @Content(schema = @Schema(implementation = UsuarioDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de registro inválidos"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El usuario ya existe"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDto> registrar(@RequestBody RegistroRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------
    @Operation(
        summary = "Iniciar sesión",
        description = "Valida credenciales y devuelve un token JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de login inválidos"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    // ---------------------------------------------------------
    // OBTENER USUARIO POR ID
    // ---------------------------------------------------------
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Obtiene los datos de un usuario específico."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = UsuarioDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // ---------------------------------------------------------
    // LISTAR TODOS LOS USUARIOS
    // ---------------------------------------------------------
    @Operation(
        summary = "Listar usuarios",
        description = "Obtiene la lista completa de usuarios."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuarios listados correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen usuarios registrados"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        List<UsuarioDto> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    // ---------------------------------------------------------
    // ACTUALIZAR USUARIO
    // ---------------------------------------------------------
    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado correctamente",
            content = @Content(schema = @Schema(implementation = UsuarioDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDto request
    ) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(
         @RequestBody CambiarPasswordRequest request,
         @RequestHeader("Authorization") String authHeader
     ) {
        usuarioService.cambiarPassword(authHeader, request);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
        }

}
