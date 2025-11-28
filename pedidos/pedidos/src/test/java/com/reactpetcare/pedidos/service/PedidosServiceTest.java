package com.reactpetcare.pedidos.service;

import com.reactpetcare.pedidos.dto.CarritoDto;
import com.reactpetcare.pedidos.dto.ItemCarritoDto;
import com.reactpetcare.pedidos.dto.ProductoResponse;
import com.reactpetcare.pedidos.dto.UsuarioResponse;
import com.reactpetcare.pedidos.model.*;
import com.reactpetcare.pedidos.repository.DetallePedidoRepository;
import com.reactpetcare.pedidos.repository.PedidoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    // 🌟 ESTA ES LA CLAVE PARA EVITAR ERRORES GENÉRICOS 🌟
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient carritoWebClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient productosWebClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient usuarioWebClient;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    // ==================================================================================
    // TEST CREAR PEDIDO
    // ==================================================================================
    @Test
    void crearPedido_exitoso() {

        Long usuarioId = 1L;

        // -----------------------------
        // Mock UsuarioResponse
        UsuarioResponse usuarioMock = new UsuarioResponse();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Francisca");
        usuarioMock.setEmail("fran@example.com");

        when(usuarioWebClient.get()
                .uri("/usuarios/{id}", usuarioId)
                .retrieve()
                .bodyToMono(UsuarioResponse.class))
                .thenReturn(Mono.just(usuarioMock));

        // -----------------------------
        // Mock CarritoDto
        ItemCarritoDto item = new ItemCarritoDto();
        item.setProductoId(10L);
        item.setCantidad(2);

        CarritoDto carrito = new CarritoDto(usuarioId, List.of(item), 20000.0);

        when(carritoWebClient.get()
                .uri("/carrito/{usuarioId}", usuarioId)
                .retrieve()
                .bodyToMono(CarritoDto.class))
                .thenReturn(Mono.just(carrito));

        // -----------------------------
        // Mock ProductoResponse
        ProductoResponse producto = new ProductoResponse();
        producto.setId(10L);
        producto.setNombre("Shampoo");
        producto.setPrecio(10000.0);
        producto.setStock(5);

        when(productosWebClient.get()
                .uri("/productos/{id}", 10L)
                .retrieve()
                .bodyToMono(ProductoResponse.class))
                .thenReturn(Mono.just(producto));

        when(productosWebClient.post()
                .uri("/productos/{id}/descontar?cantidad={cantidad}", 10L, 2)
                .retrieve()
                .toBodilessEntity())
                .thenReturn(Mono.empty());

        // -----------------------------
        // Mock guardado pedido
        Pedido pedidoGuardado = Pedido.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedidoGuardado);

        // -----------------------------
        Pedido resultado = pedidoService.crearPedido(usuarioId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
    }



    // ==================================================================================
    // TEST CAMBIAR ESTADO
    // ==================================================================================
    @Test
    void cambiarEstado_exitoso() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);

        Pedido actualizado = pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO);

        assertEquals(EstadoPedido.ENTREGADO, actualizado.getEstado());
    }


    // ==================================================================================
    // TEST OBTENER POR ID
    // ==================================================================================
    @Test
    void obtenerPorId_exitoso() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }


    // ==================================================================================
    // TEST LISTAR POR USUARIO
    // ==================================================================================
    @Test
    void listarPorUsuario_exitoso() {
        when(pedidoRepository.findByUsuarioId(5L))
                .thenReturn(List.of(new Pedido(), new Pedido()));

        List<Pedido> lista = pedidoService.listarPorUsuario(5L);

        assertEquals(2, lista.size());
    }


    // ==================================================================================
    // TEST LISTAR TODOS
    // ==================================================================================
    @Test
    void listar_exitoso() {
        when(pedidoRepository.findAll())
                .thenReturn(List.of(new Pedido(), new Pedido(), new Pedido()));

        List<Pedido> lista = pedidoService.listar();

        assertEquals(3, lista.size());
    }
}
