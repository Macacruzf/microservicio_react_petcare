package com.reactpetcare.usuario.dto;

import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String passwordActual;
    private String passwordNueva;
}
