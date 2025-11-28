package com.reactpetcare.productos.service;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.*;
import com.reactpetcare.productos.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepo;

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoria;
    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Accesorios");

        producto = Producto.builder()
                .id(1L)
                .nombre("Collar")
                .precio(5000.0)
                .descripcion("Collar de cuero")
                .categoria(categoria)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build();
    }

    // ============================================================
    // TEST CREAR
    // ============================================================
    @Test
    void crear_debeCrearProducto() {
        ProductoDto dto = new ProductoDto();
        dto.setNombre("Collar");
        dto.setPrecio(5000.0);
        dto.setDescripcion("Collar de cuero");
        dto.setCategoriaId(1L);
        dto.setStock(10);

        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);

        ProductoDto result = productoService.crear(dto);

        assertEquals(1L, result.getId());
        assertEquals(EstadoProducto.DISPONIBLE, result.getEstado());
        verify(productoRepo).save(any(Producto.class));
    }

    @Test
    void crear_deberiaFallarSiCategoriaNoExiste() {
        ProductoDto dto = new ProductoDto();
        dto.setCategoriaId(99L);

        when(categoriaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.crear(dto));
    }

    // ============================================================
    // TEST LISTAR
    // ============================================================
    @Test
    void listar_debeRetornarLista() {
        when(productoRepo.findAll()).thenReturn(List.of(producto));

        List<Producto> lista = productoService.listar();

        assertEquals(1, lista.size());
        verify(productoRepo).findAll();
    }

    // ============================================================
    // TEST OBTENER POR ID
    // ============================================================
    @Test
    void obtenerPorId_debeRetornarProducto() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));

        Producto result = productoService.obtenerPorId(1L);

        assertEquals("Collar", result.getNombre());
        verify(productoRepo).findById(1L);
    }

    @Test
    void obtenerPorId_debeLanzarExcepcionSiNoExiste() {
        when(productoRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.obtenerPorId(1L));
    }

    // ============================================================
    // TEST ACTUALIZAR
    // ============================================================
    @Test
    void actualizar_debeActualizarProducto() {
        ProductoDto dto = new ProductoDto();
        dto.setNombre("Nuevo Collar");
        dto.setDescripcion("Actualizado");
        dto.setPrecio(7990.0);
        dto.setStock(3);
        dto.setCategoriaId(1L);

        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepo.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoDto result = productoService.actualizar(1L, dto);

        assertEquals("Nuevo Collar", result.getNombre());
        assertEquals(EstadoProducto.DISPONIBLE, result.getEstado());
        verify(productoRepo).save(any(Producto.class));
    }

    @Test
    void actualizar_fallaSiCategoriaNoExiste() {
        ProductoDto dto = new ProductoDto();
        dto.setCategoriaId(99L);

        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.actualizar(1L, dto));
    }

    // ============================================================
    // TEST ELIMINAR
    // ============================================================
    @Test
    void eliminar_debeEliminarProducto() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        doNothing().when(productoRepo).delete(producto);

        productoService.eliminar(1L);

        verify(productoRepo).delete(producto);
    }

    @Test
    void eliminar_fallaSiNoExiste() {
        when(productoRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.eliminar(1L));
    }

    // ============================================================
    // TEST DESCONTAR STOCK
    // ============================================================
    @Test
    void descontarStock_debeDescontarCorrectamente() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));

        productoService.descontarStock(1L, 5);

        assertEquals(5, producto.getStock());
        verify(productoRepo).save(producto);
    }

    @Test
    void descontarStock_fallaSiStockEsCero() {
        producto.setStock(0);
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> productoService.descontarStock(1L, 1));
    }

    @Test
    void descontarStock_fallaSiStockInsuficiente() {
        producto.setStock(2);
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> productoService.descontarStock(1L, 5));
    }
}
