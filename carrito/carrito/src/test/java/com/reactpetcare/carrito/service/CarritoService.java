package com.reactpetcare.carrito.service;

import com.reactpetcare.carrito.dto.CarritoDto;
import com.reactpetcare.carrito.dto.CarritoItemDto;
import com.reactpetcare.carrito.dto.ProductoResponse;
import com.reactpetcare.carrito.model.Carrito;
import com.reactpetcare.carrito.model.CarritoItem;
import com.reactpetcare.carrito.repository.CarritoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient productosWebClient;

    @InjectMocks
    private CarritoService carritoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    // ============================================================================================
    // TEST obtenerCarrito()
    // ============================================================================================
    @Test
    void obtenerCarrito_creaCarritoSiNoExiste() {

        Long usuarioId = 1L;

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.empty());

        Carrito carritoNuevo = Carrito.builder()
                .usuarioId(usuarioId)
                .items(new ArrayList<>())
                .build();

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carritoNuevo);

        CarritoDto resultado = carritoService.obtenerCarrito(usuarioId);

        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(0, resultado.getItems().size());
    }


    @Test
    void obtenerCarrito_retornaCarritoExistente() {

        Long usuarioId = 2L;

        Carrito carrito = Carrito.builder()
                .usuarioId(usuarioId)
                .items(List.of(
                        CarritoItem.builder()
                                .productoId(5L)
                                .cantidad(3)
                                .build()
                ))
                .build();

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.of(carrito));

        CarritoDto resultado = carritoService.obtenerCarrito(usuarioId);

        assertEquals(1, resultado.getItems().size());
        assertEquals(5L, resultado.getItems().get(0).getProductoId());
        assertEquals(3, resultado.getItems().get(0).getCantidad());
    }


    // ============================================================================================
    // TEST agregarProducto()
    // ============================================================================================
    @Test
    void agregarProducto_exitoso() {

        Long usuarioId = 3L;

        // ---------------- Mock producto desde productos-service ----------------
        ProductoResponse producto = new ProductoResponse();
        producto.setId(10L);
        producto.setNombre("Shampoo");
        producto.setEstado("DISPONIBLE");

        when(productosWebClient.get()
                .uri("/productos/" + 10L)
                .retrieve()
                .bodyToMono(ProductoResponse.class))
                .thenReturn(Mono.just(producto));

        // ---------------- Carrito existente ----------------
        Carrito carrito = Carrito.builder()
                .usuarioId(usuarioId)
                .items(new ArrayList<>())
                .build();

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.of(carrito));

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carrito);

        // ---------------- Ejecutar servicio ----------------
        CarritoItemDto request = new CarritoItemDto(10L, 2);

        CarritoDto resultado = carritoService.agregarProducto(usuarioId, request);

        assertNotNull(resultado);
        assertEquals(1, resultado.getItems().size());
        assertEquals(10L, resultado.getItems().get(0).getProductoId());
        assertEquals(2, resultado.getItems().get(0).getCantidad());
    }


    // ============================================================================================
    // TEST vaciarCarrito()
    // ============================================================================================
    @Test
    void vaciarCarrito_exitoso() {

        Long usuarioId = 4L;

        Carrito carrito = Carrito.builder()
                .usuarioId(usuarioId)
                .items(new ArrayList<>(List.of(
                        CarritoItem.builder().productoId(1L).cantidad(5).build(),
                        CarritoItem.builder().productoId(2L).cantidad(1).build()
                )))
                .build();

        when(carritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(Optional.of(carrito));

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carrito);

        carritoService.vaciarCarrito(usuarioId);

        assertEquals(0, carrito.getItems().size());
    }
}
