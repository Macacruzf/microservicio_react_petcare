package com.reactpetcare.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.EstadoProducto;
import com.reactpetcare.productos.model.Producto;
import com.reactpetcare.productos.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------------------------------------------
    // LISTAR → 200
    // ---------------------------------------------------------
    @Test
    void listarProductos_ok_200() throws Exception {

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Collar")
                .precio(5000.0)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build();

        when(productoService.listar()).thenReturn(List.of(producto));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ---------------------------------------------------------
    // OBTENER POR ID → 200
    // ---------------------------------------------------------
    @Test
    void obtenerProductoPorId_ok_200() throws Exception {

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Collar")
                .precio(5000.0)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build();

        when(productoService.obtenerPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ---------------------------------------------------------
    // CREAR → 201 (Swagger) / 200 real
    // ---------------------------------------------------------
    @Test
    void crearProducto_ok() throws Exception {

        ProductoDto request = new ProductoDto();
        request.setNombre("Shampoo");
        request.setPrecio(5990.0);
        request.setStock(15);
        request.setEstado(EstadoProducto.DISPONIBLE);

        ProductoDto response = new ProductoDto();
        response.setId(1L);
        response.setNombre("Shampoo");

        when(productoService.crear(any())).thenReturn(response);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ---------------------------------------------------------
    // ACTUALIZAR → 200
    // ---------------------------------------------------------
    @Test
    void actualizarProducto_ok_200() throws Exception {

        ProductoDto request = new ProductoDto();
        request.setNombre("Nuevo nombre");

        ProductoDto response = new ProductoDto();
        response.setId(1L);
        response.setNombre("Nuevo nombre");

        when(productoService.actualizar(eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo nombre"));
    }

    // ---------------------------------------------------------
    // ELIMINAR → 204
    // ---------------------------------------------------------
    @Test
    void eliminarProducto_noContent_204() throws Exception {

        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());
    }

    // ---------------------------------------------------------
    // DESCONTAR STOCK → 200
    // ---------------------------------------------------------
    @Test
    void descontarStock_ok_200() throws Exception {

        doNothing().when(productoService).descontarStock(1L, 2);

        mockMvc.perform(post("/productos/1/descontar")
                        .param("cantidad", "2"))
                .andExpect(status().isOk());
    }
}
