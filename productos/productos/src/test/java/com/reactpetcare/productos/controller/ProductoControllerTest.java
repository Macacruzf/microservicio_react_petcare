package com.reactpetcare.productos.controller;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.Producto;
import com.reactpetcare.productos.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // LISTAR
    // ---------------------------------------------------------
    @Test
    void listar_debeRetornarListaDeProductos() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Shampoo");

        when(productoService.listar()).thenReturn(List.of(p));

        ResponseEntity<List<Producto>> response = productoController.listar();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Shampoo", response.getBody().get(0).getNombre());
        verify(productoService).listar();
    }

    // ---------------------------------------------------------
    // OBTENER POR ID
    // ---------------------------------------------------------
    @Test
    void obtenerPorId_debeRetornarProducto() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Shampoo");

        when(productoService.obtenerPorId(1L)).thenReturn(p);

        ResponseEntity<Producto> response = productoController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Shampoo", response.getBody().getNombre());
        verify(productoService).obtenerPorId(1L);
    }

    // ---------------------------------------------------------
    // CREAR PRODUCTO
    // ---------------------------------------------------------
    @Test
    void crear_debeCrearProducto() {
        ProductoDto dto = new ProductoDto();
        dto.setNombre("Collar");
        dto.setPrecio(5000.0);

        when(productoService.crear(dto)).thenReturn(dto);

        ResponseEntity<ProductoDto> response = productoController.crear(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Collar", response.getBody().getNombre());
        verify(productoService).crear(dto);
    }

    // ---------------------------------------------------------
    // ACTUALIZAR PRODUCTO
    // ---------------------------------------------------------
    @Test
    void actualizar_debeModificarProducto() {
        ProductoDto dto = new ProductoDto();
        dto.setNombre("Collar Premium");
        dto.setPrecio(7990.0);

        when(productoService.actualizar(1L, dto)).thenReturn(dto);

        ResponseEntity<ProductoDto> response = productoController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Collar Premium", response.getBody().getNombre());
        verify(productoService).actualizar(1L, dto);
    }

    // ---------------------------------------------------------
    // ELIMINAR PRODUCTO
    // ---------------------------------------------------------
    @Test
    void eliminar_debeEliminarProducto() {
        doNothing().when(productoService).eliminar(1L);

        ResponseEntity<Void> response = productoController.eliminar(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productoService).eliminar(1L);
    }

    // ---------------------------------------------------------
    // DESCONTAR STOCK
    // ---------------------------------------------------------
    @Test
    void descontarStock_debeDescontarCantidad() {
        doNothing().when(productoService).descontarStock(1L, 5);

        ResponseEntity<Void> response = productoController.descontarStock(1L, 5);

        assertEquals(200, response.getStatusCodeValue());
        verify(productoService).descontarStock(1L, 5);
    }
}
