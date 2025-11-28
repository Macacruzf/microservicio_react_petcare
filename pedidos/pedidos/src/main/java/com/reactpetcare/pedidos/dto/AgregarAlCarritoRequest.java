package com.reactpetcare.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor        // constructor con usuarioId + items
@NoArgsConstructor 
public class AgregarAlCarritoRequest {

    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
}
