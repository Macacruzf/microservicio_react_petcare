package com.reactpetcare.usuario.controller;

import com.reactpetcare.usuario.dto.*;
import com.reactpetcare.usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(
    name = "Usuarios",
    description = "API de gestión interna de usuarios con autenticación JWT"
)
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ---------------------------------------------------------
    // REGISTRO
    // ---------------------------------------------------------
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea un usuario con rol CLIENTE por defecto y devuelve sus datos."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario registrado correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioDto.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos de registro inválidos"
    )
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDto> registrar(
            @RequestBody RegistroRequest request
    ) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------
    @Operation(
        summary = "Iniciar sesión",
        description = "Valida credenciales y devuelve un token JWT."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Login exitoso",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LoginResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "401",
        description = "Credenciales incorrectas"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    // ---------------------------------------------------------
    // OBTENER POR ID
    // ---------------------------------------------------------
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Devuelve los datos del usuario asociado al ID."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioDto.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // ---------------------------------------------------------
    // LISTAR TODOS
    // ---------------------------------------------------------
    @Operation(
        summary = "Listar todos los usuarios",
        description = "Devuelve la lista completa de usuarios registrados."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Listado obtenido correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioDto.class)
        )
    )
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // ---------------------------------------------------------
    // ACTUALIZAR USUARIO
    // ---------------------------------------------------------
    @Operation(
        summary = "Actualizar usuario",
        description = "Permite editar los datos del usuario."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario actualizado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioDto.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDto request
    ) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    // ---------------------------------------------------------
    // CAMBIAR CONTRASEÑA
    // ---------------------------------------------------------
    @Operation(
        summary = "Cambiar contraseña",
        description = "Permite al usuario cambiar su contraseña usando JWT."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Contraseña actualizada correctamente"
    )
    @ApiResponse(
        responseCode = "401",
        description = "Token inválido o contraseña incorrecta"
    )
    @PutMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CambiarPasswordRequest request
    ) {
        usuarioService.cambiarPassword(authHeader, request);
        return ResponseEntity.ok().build();
    }
}
