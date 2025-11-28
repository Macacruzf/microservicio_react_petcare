package com.reactpetcare.usuario.controller;

import com.reactpetcare.usuario.dto.*;
import com.reactpetcare.usuario.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // TEST: REGISTRO
    // ---------------------------------------------------------
    @Test
    void registrar_debeCrearUsuario() {
        RegistroRequest request = new RegistroRequest();
        request.setEmail("correo@test.com");
        request.setPassword("1234");

        UsuarioDto dto = new UsuarioDto();
        dto.setId(1L);
        dto.setEmail("correo@test.com");

        when(usuarioService.registrar(request)).thenReturn(dto);

        ResponseEntity<UsuarioDto> response = usuarioController.registrar(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
        verify(usuarioService).registrar(request);
    }

    // ---------------------------------------------------------
    // TEST: LOGIN (usando tu LoginResponse real sin token)
    // ---------------------------------------------------------
    @Test
    void login_debeRetornarDatosUsuario() {
        LoginRequest request = new LoginRequest();
        request.setEmail("correo@test.com");
        request.setPassword("1234");

        LoginResponse loginResponse = new LoginResponse(
                1L,
                "Francisca",
                "González",
                "francisca@example.com",
                "CLIENTE"
        );

        when(usuarioService.login(request)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = usuarioController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getUserId());
        assertEquals("Francisca", response.getBody().getNombre());
        assertEquals("González", response.getBody().getApellido());
        assertEquals("francisca@example.com", response.getBody().getEmail());
        assertEquals("CLIENTE", response.getBody().getRol());

        verify(usuarioService).login(request);
    }

    // ---------------------------------------------------------
    // TEST: OBTENER POR ID
    // ---------------------------------------------------------
    @Test
    void obtenerPorId_debeRetornarUsuario() {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(1L);
        dto.setNombre("Francisca");

        when(usuarioService.obtenerPorId(1L)).thenReturn(dto);

        ResponseEntity<UsuarioDto> response = usuarioController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Francisca", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
        verify(usuarioService).obtenerPorId(1L);
    }

    // ---------------------------------------------------------
    // TEST: LISTAR TODOS
    // ---------------------------------------------------------
    @Test
    void listar_debeRetornarListaUsuarios() {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(1L);

        List<UsuarioDto> lista = List.of(dto);

        when(usuarioService.listar()).thenReturn(lista);

        ResponseEntity<List<UsuarioDto>> response = usuarioController.listar();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(usuarioService).listar();
    }

    // ---------------------------------------------------------
    // TEST: ACTUALIZAR USUARIO
    // ---------------------------------------------------------
    @Test
    void actualizar_debeActualizarUsuario() {
        UsuarioDto request = new UsuarioDto();
        request.setNombre("Nuevo Nombre");

        UsuarioDto actualizado = new UsuarioDto();
        actualizado.setId(1L);
        actualizado.setNombre("Nuevo Nombre");

        when(usuarioService.actualizar(1L, request)).thenReturn(actualizado);

        ResponseEntity<UsuarioDto> response = usuarioController.actualizar(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Nuevo Nombre", response.getBody().getNombre());
        verify(usuarioService).actualizar(1L, request);
    }
}
