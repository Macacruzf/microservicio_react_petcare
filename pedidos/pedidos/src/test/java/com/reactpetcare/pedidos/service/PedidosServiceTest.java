package com.reactpetcare.pedidos.service;

import com.reactpetcare.pedidos.dto.*;
import com.reactpetcare.pedidos.model.*;
import com.reactpetcare.pedidos.repository.DetallePedidoRepository;
import com.reactpetcare.pedidos.repository.PedidoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @Mock
    private WebClient carritoWebClient;

    @Mock
    private WebClient productosWebClient;

    @Mock
    private WebClient usuarioWebClient;

    @InjectMocks
    private PedidoService pedidoService;

    // ===== WebClient chains =====
    @Mock private WebClient.RequestHeadersUriSpec<?> usuarioGet;
    @Mock private WebClient.RequestHeadersUriSpec<?> carritoGet;
    @Mock private WebClient.RequestHeadersUriSpec<?> productoGet;
    @Mock private WebClient.RequestHeadersUriSpec<?> carritoDelete;

    @Mock private WebClient.RequestBodyUriSpec productoPost;
    @Mock private WebClient.RequestHeadersSpec<?> headersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // =====================================================
    // CREAR PEDIDO OK
    // =====================================================
    @Test
    void crearPedido_ok() {

        Long usuarioId = 1L;

        // ---------- USUARIO ----------
        UsuarioResponse usuario = new UsuarioResponse();
        usuario.setId(1L);
        usuario.setNombre("Francisca");
        usuario.setEmail("francisca@example.com");

        doReturn(usuarioGet).when(usuarioWebClient).get();
        doReturn(headersSpec).when(usuarioGet).uri("/usuarios/{id}", usuarioId);
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.just(usuario))
                .when(responseSpec).bodyToMono(UsuarioResponse.class);

        // ---------- CARRITO ----------
        ItemCarritoDto item = new ItemCarritoDto();
        item.setProductoId(10L);
        item.setCantidad(2);

        CarritoDto carrito = new CarritoDto();
        carrito.setItems(List.of(item));

        doReturn(carritoGet).when(carritoWebClient).get();
        doReturn(headersSpec).when(carritoGet).uri("/carrito/{usuarioId}", usuarioId);
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.just(carrito))
                .when(responseSpec).bodyToMono(CarritoDto.class);

        // ---------- PRODUCTO ----------
        ProductoResponse producto = new ProductoResponse();
        producto.setId(10L);
        producto.setNombre("Shampoo");
        producto.setPrecio(5000.0);
        producto.setStock(10);

        doReturn(productoGet).when(productosWebClient).get();
        doReturn(headersSpec).when(productoGet).uri("/productos/{id}", 10L);
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.just(producto))
                .when(responseSpec).bodyToMono(ProductoResponse.class);

        // ---------- DESCONTAR STOCK ----------
        doReturn(productoPost).when(productosWebClient).post();
        doReturn(headersSpec).when(productoPost)
                .uri("/productos/{id}/descontar?cantidad={cantidad}", 10L, 2);
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.empty()).when(responseSpec).toBodilessEntity();

        // ---------- GUARDAR PEDIDO ----------
        doAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(1L);
            p.setFechaCreacion(LocalDateTime.now());
            return p;
        }).when(pedidoRepository).save(any(Pedido.class));

        // ---------- VACIAR CARRITO ----------
        doReturn(carritoDelete).when(carritoWebClient).delete();
        doReturn(headersSpec).when(carritoDelete)
                .uri("/carrito/{usuarioId}/vaciar", usuarioId);
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.empty()).when(responseSpec).toBodilessEntity();

        // ---------- EJECUCIÓN ----------
        Pedido resultado = pedidoService.crearPedido(usuarioId);

        // ---------- ASSERT ----------
        assertNotNull(resultado);
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        assertEquals(10000.0, resultado.getTotal());

        verify(pedidoRepository, atLeastOnce()).save(any(Pedido.class));
        verify(detallePedidoRepository).saveAll(anyList());
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @Test
    void obtenerPorId_ok() {

        Pedido pedido = Pedido.builder()
                .id(5L)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        when(pedidoRepository.findById(5L))
                .thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.obtenerPorId(5L);

        assertEquals(5L, resultado.getId());
    }

    // =====================================================
    // CAMBIAR ESTADO
    // =====================================================
    @Test
    void cambiarEstado_ok() {

        Pedido pedido = Pedido.builder()
                .id(3L)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        when(pedidoRepository.findById(3L))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(i -> i.getArgument(0));

        Pedido actualizado = pedidoService.cambiarEstado(3L, EstadoPedido.ENTREGADO);

        assertEquals(EstadoPedido.ENTREGADO, actualizado.getEstado());
    }
}
