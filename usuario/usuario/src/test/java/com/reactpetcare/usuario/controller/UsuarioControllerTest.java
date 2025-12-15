package com.reactpetcare.usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactpetcare.usuario.dto.*;
import com.reactpetcare.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------------------------------------------
    // REGISTRO → 201
    // ---------------------------------------------------------
    @Test
    void registrarUsuario_ok_201() throws Exception {
        RegistroRequest request = new RegistroRequest();
        request.setNombre("Francisca");

        UsuarioDto response = new UsuarioDto();
        response.setId(1L);
        response.setNombre("Francisca");

        Mockito.when(usuarioService.registrar(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ---------------------------------------------------------
    // LOGIN → 200
    // ---------------------------------------------------------
    @Test
    void login_ok_200() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("francisca@example.com");
        request.setPassword("password");

        LoginResponse response = new LoginResponse(
                1L,
                "Francisca",
                "Castro",
                "francisca@example.com",
                "CLIENTE",
                "jwt-token"
        );

        Mockito.when(usuarioService.login(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    // ---------------------------------------------------------
    // OBTENER POR ID → 200
    // ---------------------------------------------------------
    @Test
    void obtenerUsuarioPorId_ok_200() throws Exception {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(1L);
        dto.setNombre("Francisca");

        Mockito.when(usuarioService.obtenerPorId(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ---------------------------------------------------------
    // LISTAR → 200
    // ---------------------------------------------------------
    @Test
    void listarUsuarios_ok_200() throws Exception {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(1L);

        Mockito.when(usuarioService.listar())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ---------------------------------------------------------
    // LISTAR → 204
    // ---------------------------------------------------------
    @Test
    void listarUsuarios_noContent_204() throws Exception {
        Mockito.when(usuarioService.listar())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isNoContent());
    }

    // ---------------------------------------------------------
    // ACTUALIZAR → 200
    // ---------------------------------------------------------
    @Test
    void actualizarUsuario_ok_200() throws Exception {
        UsuarioDto request = new UsuarioDto();
        request.setNombre("Nuevo nombre");

        UsuarioDto response = new UsuarioDto();
        response.setId(1L);
        response.setNombre("Nuevo nombre");

        Mockito.when(usuarioService.actualizar(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo nombre"));
    }
}
