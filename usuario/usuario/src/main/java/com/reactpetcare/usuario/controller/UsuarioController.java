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
            description = "Devuelve datos del usuario."
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
            description = "Devuelve la lista completa de usuarios.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = "application/json"))
            }
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
            description = "Edita datos del usuario."
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDto request
    ) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }
}
