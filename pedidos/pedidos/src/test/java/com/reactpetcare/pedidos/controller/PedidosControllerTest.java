package com.reactpetcare.pedidos.controller;

import com.reactpetcare.pedidos.model.EstadoPedido;
import com.reactpetcare.pedidos.model.Pedido;
import com.reactpetcare.pedidos.service.PedidoService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    // =====================================================
    // CREAR PEDIDO
    // =====================================================
    @Test
    void crearPedido_ok() throws Exception {

        Pedido pedido = Pedido.builder()
                .id(1L)
                .usuarioId(1L)
                .nombreUsuario("Francisca Castro")
                .emailUsuario("francisca@example.com")
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoPedido.PENDIENTE)
                .total(25990.0)
                .build();

        Mockito.when(pedidoService.crearPedido(1L))
                .thenReturn(pedido);

        mockMvc.perform(post("/pedidos/crear/{usuarioId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @Test
    void obtenerPorId_ok() throws Exception {

        Pedido pedido = Pedido.builder()
                .id(10L)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        Mockito.when(pedidoService.obtenerPorId(10L))
                .thenReturn(pedido);

        mockMvc.perform(get("/pedidos/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================
    @Test
    void listar_ok() throws Exception {

        List<Pedido> pedidos = List.of(
                Pedido.builder().id(1L).build(),
                Pedido.builder().id(2L).build()
        );

        Mockito.when(pedidoService.listar())
                .thenReturn(pedidos);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================
    @Test
    void listarPorUsuario_ok() throws Exception {

        List<Pedido> pedidos = List.of(
                Pedido.builder().id(1L).usuarioId(1L).build()
        );

        Mockito.when(pedidoService.listarPorUsuario(1L))
                .thenReturn(pedidos);

        mockMvc.perform(get("/pedidos/usuario/{usuarioId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1L));
    }

    // =====================================================
    // CAMBIAR ESTADO
    // =====================================================
    @Test
    void cambiarEstado_ok() throws Exception {

        Pedido pedido = Pedido.builder()
                .id(10L)
                .estado(EstadoPedido.ENTREGADO)
                .build();

        Mockito.when(pedidoService.cambiarEstado(10L, EstadoPedido.ENTREGADO))
                .thenReturn(pedido);

        mockMvc.perform(
                put("/pedidos/{id}/estado", 10L)
                        .param("estado", "ENTREGADO")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }
}
