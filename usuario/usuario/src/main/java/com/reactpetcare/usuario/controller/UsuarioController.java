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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API de gestión interna de usuarios con autenticación JWT")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ---------------------------------------------------------
    // REGISTRO
    // ---------------------------------------------------------
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un usuario con rol CLIENTE por defecto. "
                        + "Devuelve datos del usuario recién creado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado",
                            content = @Content(schema = @Schema(implementation = UsuarioDto.class))),
            }
    )
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDto> registrar(@RequestBody RegistroRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida credenciales y devuelve un token JWT para autenticación.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login exitoso",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    // ---------------------------------------------------------
    // OBTENER POR ID
    // ---------------------------------------------------------
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Devuelve datos del usuario. Requiere un token válido."
    )
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // ---------------------------------------------------------
    // LISTAR TODOS (ADMIN)
    // ---------------------------------------------------------
    @Operation(
            summary = "Listar todos los usuarios",
            description = "Solo ADMIN puede ver la lista completa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "No autorizado")
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // ---------------------------------------------------------
    // ACTUALIZAR USUARIO (ADMIN)
    // ---------------------------------------------------------
    @Operation(
            summary = "Actualizar usuario",
            description = "Solo ADMIN puede editar datos del usuario."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDto request
    ) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }
}
