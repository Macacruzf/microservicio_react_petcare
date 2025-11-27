package com.reactpetcare.pedidos.dto;

import lombok.Data;

@Data
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}
