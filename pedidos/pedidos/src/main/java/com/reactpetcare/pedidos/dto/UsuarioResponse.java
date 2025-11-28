package com.reactpetcare.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   // ← genera constructor con todos los campos
@NoArgsConstructor 
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
}
