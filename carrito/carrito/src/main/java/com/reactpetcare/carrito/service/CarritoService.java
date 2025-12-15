package com.reactpetcare.carrito.service;

import com.reactpetcare.carrito.dto.CarritoDto;
import com.reactpetcare.carrito.dto.CarritoItemDto;
import com.reactpetcare.carrito.dto.ProductoResponse;
import com.reactpetcare.carrito.model.Carrito;
import com.reactpetcare.carrito.model.CarritoItem;
import com.reactpetcare.carrito.repository.CarritoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;

    /** WebClient hacia productos-service */
    private final WebClient productosWebClient;

    // ────────────────────────────────────────────────────────────────
    /** Obtener carrito del usuario */
    public CarritoDto obtenerCarrito(Long usuarioId) {

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> carritoRepository.save(
                        Carrito.builder()
                                .usuarioId(usuarioId)
                                .build()
                ));

        return CarritoDto.builder()
                .usuarioId(usuarioId)
                .items(
                        carrito.getItems().stream()
                                .map(item -> new CarritoItemDto(
                                        item.getProductoId(),
                                        item.getCantidad()
                                ))
                                .collect(Collectors.toList())
                )
                .build();
    }

    // ────────────────────────────────────────────────────────────────
    /** Agregar producto al carrito con validación desde productos-service */
    public CarritoDto agregarProducto(Long usuarioId, CarritoItemDto request) {

        // Validación desde productos-service
        ProductoResponse producto = productosWebClient.get()
                .uri("/productos/" + request.getProductoId())
                .retrieve()
                .bodyToMono(ProductoResponse.class)
                .block();

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        if (producto.getEstado().equalsIgnoreCase("SIN_STOCK")) {
            throw new RuntimeException("Producto sin stock");
        }

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> carritoRepository.save(
                        Carrito.builder().usuarioId(usuarioId).build()
                ));

        // buscar o crear item
        Optional<CarritoItem> existente = carrito.getItems().stream()
                .filter(i -> i.getProductoId().equals(request.getProductoId()))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(
                    existente.get().getCantidad() + request.getCantidad()
            );
        } else {
            carrito.getItems().add(
                    CarritoItem.builder()
                            .productoId(request.getProductoId())
                            .cantidad(request.getCantidad())
                            .carrito(carrito)
                            .build()
            );
        }

        carritoRepository.save(carrito);

        return obtenerCarrito(usuarioId);
    }

    // ────────────────────────────────────────────────────────────────
    /** Vaciar carrito del usuario */
    public void vaciarCarrito(Long usuarioId) {
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuarioId);
        
        if (carritoOpt.isPresent()) {
            Carrito carrito = carritoOpt.get();
            // Eliminar todos los items (orphanRemoval se encargará de borrarlos de BD)
            carrito.getItems().clear();
            carritoRepository.save(carrito);
            System.out.println("🗑️ Carrito vaciado para usuario: " + usuarioId);
        } else {
            // Si no existe carrito, no hay nada que vaciar
            System.out.println("ℹ️ No existe carrito para usuario: " + usuarioId);
        }
    }
}
