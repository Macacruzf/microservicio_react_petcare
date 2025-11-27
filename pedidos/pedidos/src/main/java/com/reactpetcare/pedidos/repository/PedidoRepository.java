package com.reactpetcare.pedidos.repository;

import com.reactpetcare.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}
