package com.reactpetcare.pedidos.dto;

import lombok.Data;

@Data
public class AgregarAlCarritoRequest {

    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
}
