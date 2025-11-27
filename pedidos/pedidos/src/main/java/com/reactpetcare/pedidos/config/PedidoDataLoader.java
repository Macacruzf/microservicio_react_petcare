package com.reactpetcare.pedidos.config;

import com.reactpetcare.pedidos.model.*;
import com.reactpetcare.pedidos.repository.PedidoRepository;
import com.reactpetcare.pedidos.repository.DetallePedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class PedidoDataLoader implements CommandLineRunner {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;

    @Override
    public void run(String... args) throws Exception {

        if (pedidoRepository.count() > 0) {
            return; // ya existen pedidos → no duplicar
        }

        // ==========================
        // PEDIDO #1 (usuario ID 1)
        // ==========================
        Pedido pedido1 = Pedido.builder()
                .usuarioId(1L)
                .nombreUsuario("Usuario Prueba 1")
                .emailUsuario("usuario1@test.cl")
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoPedido.ENTREGADO)
                .build();

        pedido1 = pedidoRepository.save(pedido1);

        DetallePedido d1 = DetallePedido.builder()
                .pedido(pedido1)
                .productoId(1L)
                .nombreProducto("Shampoo Para Perros")
                .cantidad(1)
                .precioUnitario(5990.0)
                .subtotal(5990.0)
                .build();

        DetallePedido d2 = DetallePedido.builder()
                .pedido(pedido1)
                .productoId(2L)
                .nombreProducto("Snacks de Pollo")
                .cantidad(2)
                .precioUnitario(2990.0)
                .subtotal(5980.0)
                .build();

        detalleRepository.saveAll(Arrays.asList(d1, d2));
        pedido1.setTotal(d1.getSubtotal() + d2.getSubtotal());
        pedidoRepository.save(pedido1);

        // ==========================
        // PEDIDO #2 (usuario ID 2)
        // ==========================
        Pedido pedido2 = Pedido.builder()
                .usuarioId(2L)
                .nombreUsuario("Usuario Prueba 2")
                .emailUsuario("usuario2@test.cl")
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoPedido.PENDIENTE)
                .build();

        pedido2 = pedidoRepository.save(pedido2);

        DetallePedido d3 = DetallePedido.builder()
                .pedido(pedido2)
                .productoId(3L)
                .nombreProducto("Plato Antideslizante")
                .cantidad(1)
                .precioUnitario(9990.0)
                .subtotal(9990.0)
                .build();

        detalleRepository.save(d3);
        pedido2.setTotal(d3.getSubtotal());
        pedidoRepository.save(pedido2);

        System.out.println("Pedidos de prueba cargados con éxito");
    }
}
