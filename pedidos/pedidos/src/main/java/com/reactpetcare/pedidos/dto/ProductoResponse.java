package com.reactpetcare.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor        // constructor con usuarioId + items
@NoArgsConstructor 
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}
