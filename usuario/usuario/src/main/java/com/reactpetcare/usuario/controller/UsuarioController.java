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
@Tag(
        name = "Usuarios",
        description = "Microservicio encargado del registro, autenticación y gestión de usuarios"
)
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ================================================================
    // REGISTRAR USUARIO
    // ================================================================
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Permite crear un usuario con rol CLIENTE por defecto. Devuelve un UsuarioDto con los datos registrados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario registrado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El email ya está registrado o los datos son inválidos"
            )
    })
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDto> registrar(
            @RequestBody RegistroRequest request
    ) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    // ================================================================
    // LOGIN
    // ================================================================
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales del usuario. Devuelve un LoginResponse con los datos básicos del usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    // ================================================================
    // OBTENER POR ID
    // ================================================================
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna los datos del usuario según el ID proporcionado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // ================================================================
    // LISTAR TODOS
    // ================================================================
    @Operation(
            summary = "Listar todos los usuarios",
            description = "Retorna una lista completa de usuarios registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida correctamente",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // ================================================================
    // ACTUALIZAR USUARIO
    // ================================================================
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos del usuario según el ID especificado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDto request
    ) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }
}
