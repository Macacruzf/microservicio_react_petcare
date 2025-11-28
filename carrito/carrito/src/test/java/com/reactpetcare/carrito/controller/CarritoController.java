package com.reactpetcare.carrito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactpetcare.carrito.dto.CarritoDto;
import com.reactpetcare.carrito.dto.CarritoItemDto;
import com.reactpetcare.carrito.service.CarritoService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    @Autowired
    private ObjectMapper mapper;

    // ============================================================
    // TEST: Obtener carrito
    // ============================================================
    @Test
    void obtenerCarrito_exitoso() throws Exception {

        Long usuarioId = 5L;

        CarritoDto response = CarritoDto.builder()
                .usuarioId(usuarioId)
                .items(List.of(
                        new CarritoItemDto(10L, 2),
                        new CarritoItemDto(20L, 1)
                ))
                .build();

        Mockito.when(carritoService.obtenerCarrito(usuarioId))
                .thenReturn(response);

        mockMvc.perform(get("/carrito/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuarioId))
                .andExpect(jsonPath("$.items[0].productoId").value(10L))
                .andExpect(jsonPath("$.items[0].cantidad").value(2))
                .andExpect(jsonPath("$.items[1].productoId").value(20L))
                .andExpect(jsonPath("$.items[1].cantidad").value(1));
    }

    // ============================================================
    // TEST: Agregar producto al carrito
    // ============================================================
    @Test
    void agregarProducto_exitoso() throws Exception {

        Long usuarioId = 7L;

        CarritoItemDto itemDto = new CarritoItemDto(33L, 1);

        CarritoDto actualizado = CarritoDto.builder()
                .usuarioId(usuarioId)
                .items(List.of(itemDto))
                .build();

        Mockito.when(carritoService.agregarProducto(eq(usuarioId), any(CarritoItemDto.class)))
                .thenReturn(actualizado);

        mockMvc.perform(post("/carrito/{usuarioId}/agregar", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "productoId": 33,
                                    "cantidad": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuarioId))
                .andExpect(jsonPath("$.items[0].productoId").value(33L))
                .andExpect(jsonPath("$.items[0].cantidad").value(1));
    }

    // ============================================================
    // TEST: Vaciar carrito
    // ============================================================
    @Test
    void vaciarCarrito_exitoso() throws Exception {

        Long usuarioId = 9L;

        mockMvc.perform(delete("/carrito/{usuarioId}/vaciar", usuarioId))
                .andExpect(status().isOk());

        Mockito.verify(carritoService, Mockito.times(1))
                .vaciarCarrito(usuarioId);
    }
}
