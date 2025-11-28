package com.reactpetcare.pedidos.controller;

import com.reactpetcare.pedidos.model.EstadoPedido;
import com.reactpetcare.pedidos.model.Pedido;
import com.reactpetcare.pedidos.service.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuarioId(5L);
        pedido.setEstado(EstadoPedido.PENDIENTE);
    }

    // ---------------------------------------------------------
    // CREAR PEDIDO
    // ---------------------------------------------------------
    @Test
    void crearPedido_debeCrearPedidoCorrectamente() {
        when(pedidoService.crearPedido(5L)).thenReturn(pedido);

        ResponseEntity<Pedido> response = pedidoController.crearPedido(5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        assertEquals(EstadoPedido.PENDIENTE, response.getBody().getEstado());
        verify(pedidoService).crearPedido(5L);
    }

    // ---------------------------------------------------------
    // OBTENER PEDIDO POR ID
    // ---------------------------------------------------------
    @Test
    void obtenerPorId_debeRetornarPedido() {
        when(pedidoService.obtenerPorId(1L)).thenReturn(pedido);

        ResponseEntity<Pedido> response = pedidoController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        verify(pedidoService).obtenerPorId(1L);
    }

    // ---------------------------------------------------------
    // LISTAR TODOS
    // ---------------------------------------------------------
    @Test
    void listar_debeRetornarListaDePedidos() {
        when(pedidoService.listar()).thenReturn(List.of(pedido));

        ResponseEntity<List<Pedido>> response = pedidoController.listar();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(pedidoService).listar();
    }

    // ---------------------------------------------------------
    // LISTAR POR USUARIO
    // ---------------------------------------------------------
    @Test
    void listarPorUsuario_debeRetornarPedidosDelUsuario() {
        when(pedidoService.listarPorUsuario(5L)).thenReturn(List.of(pedido));

        ResponseEntity<List<Pedido>> response = pedidoController.listarPorUsuario(5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(pedidoService).listarPorUsuario(5L);
    }

    // ---------------------------------------------------------
    // CAMBIAR ESTADO
    // ---------------------------------------------------------
    @Test
    void cambiarEstado_debeActualizarEstadoDelPedido() {
        Pedido actualizado = new Pedido();
        actualizado.setId(1L);
        actualizado.setEstado(EstadoPedido.ENTREGADO);

        when(pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO))
                .thenReturn(actualizado);

        ResponseEntity<Pedido> response =
                pedidoController.cambiarEstado(1L, EstadoPedido.ENTREGADO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(EstadoPedido.ENTREGADO, response.getBody().getEstado());
        verify(pedidoService).cambiarEstado(1L, EstadoPedido.ENTREGADO);
    }
}
