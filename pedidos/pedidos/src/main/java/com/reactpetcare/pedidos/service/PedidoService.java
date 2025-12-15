package com.reactpetcare.pedidos.service;

import com.reactpetcare.pedidos.dto.CarritoDto;
import com.reactpetcare.pedidos.dto.ItemCarritoDto;
import com.reactpetcare.pedidos.dto.ProductoResponse;
import com.reactpetcare.pedidos.dto.UsuarioResponse;
import com.reactpetcare.pedidos.model.*;
import com.reactpetcare.pedidos.repository.PedidoRepository;
import com.reactpetcare.pedidos.repository.DetallePedidoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    /** WebClients hacia otros microservicios */
    private final WebClient carritoWebClient;
    private final WebClient productosWebClient;
    private final WebClient usuarioWebClient;

    /**
     * Crear un pedido desde el carrito del usuario
     */
    @Transactional
    public Pedido crearPedido(Long usuarioId) {

        UsuarioResponse usuario;
        CarritoDto carrito;

        try {
            // 1. Validar existencia del usuario
            usuario = usuarioWebClient.get()
                    .uri("/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();

            if (usuario == null) {
                throw new RuntimeException("El usuario no existe");
            }

            // 2. Obtener carrito del usuario
            carrito = carritoWebClient.get()
                    .uri("/carrito/{usuarioId}", usuarioId)
                    .retrieve()
                    .bodyToMono(CarritoDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.err.println("Error al comunicar con microservicios: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al obtener datos de usuario o carrito: " + e.getStatusCode());
        }

        if (carrito == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // 3. Crear objeto pedido
        Pedido pedidoGuardado = pedidoRepository.save(
                Pedido.builder()
                        .usuarioId(usuarioId)
                        .nombreUsuario(usuario.getNombre())
                        .emailUsuario(usuario.getEmail())
                        .fechaCreacion(LocalDateTime.now())
                        .estado(EstadoPedido.PENDIENTE)
                        .build()
        );

        // 4. Procesar ítems → crear detalles
        List<DetallePedido> detalles = carrito.getItems().stream()
                .map(item -> procesarItemCarrito(item, pedidoGuardado))
                .collect(Collectors.toList());

        detallePedidoRepository.saveAll(detalles);

        // 5. Calcular total
        double total = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();

        pedidoGuardado.setDetalles(detalles);
        pedidoGuardado.setTotal(total);

        pedidoRepository.save(pedidoGuardado);

        // 6. Vaciar carrito
        carritoWebClient.delete()
                .uri("/carrito/{usuarioId}/vaciar", usuarioId)
                .retrieve()
                .toBodilessEntity()
                .block();

        return pedidoGuardado;
    }

    /**
     * Procesa un ítem del carrito:
     * - obtiene producto
     * - valida stock
     * - descuenta stock
     * - crea detalle del pedido
     */
    private DetallePedido procesarItemCarrito(ItemCarritoDto item, Pedido pedido) {

        ProductoResponse producto;
        try {
            // Obtener datos del producto desde productos-service
            producto = productosWebClient.get()
                    .uri("/productos/{id}", item.getProductoId())
                    .retrieve()
                    .bodyToMono(ProductoResponse.class)
                    .block();

            if (producto == null) {
                throw new RuntimeException("Producto no encontrado en productos-service");
            }

            // Valida stock
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente de " + producto.getNombre());
            }

            // Descontar stock en productos-service
            productosWebClient.post()
                    .uri("/productos/{id}/descontar?cantidad={cantidad}",
                            item.getProductoId(), item.getCantidad())
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al procesar producto " + item.getProductoId() + ": " + e.getStatusCode());
        }

        // Crear detalle del pedido
        double subtotal = producto.getPrecio() * item.getCantidad();

        return DetallePedido.builder()
                .pedido(pedido)
                .productoId(producto.getId())
                .nombreProducto(producto.getNombre())
                .cantidad(item.getCantidad())
                .precioUnitario(producto.getPrecio())
                .subtotal(subtotal)
                .build();
    }

    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }
}
