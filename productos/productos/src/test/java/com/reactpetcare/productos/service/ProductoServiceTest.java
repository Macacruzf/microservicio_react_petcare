package com.reactpetcare.productos.service;

import com.reactpetcare.productos.dto.ProductoDto;
import com.reactpetcare.productos.model.*;
import com.reactpetcare.productos.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepo;

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private ProductoService productoService;

    // =====================================================
    // CREAR PRODUCTO → OK
    // =====================================================
    @Test
    void crearProducto_ok() {

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("Accesorios")
                .build();

        ProductoDto dto = new ProductoDto();
        dto.setNombre("Collar");
        dto.setPrecio(5000.0);
        dto.setDescripcion("Collar rojo");
        dto.setCategoriaId(1L);
        dto.setStock(10);
        dto.setImagen("img");

        when(categoriaRepo.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(productoRepo.save(any()))
                .thenAnswer(inv -> {
                    Producto p = inv.getArgument(0);
                    p.setId(1L);
                    return p;
                });

        ProductoDto resultado = productoService.crear(dto);

        assertNotNull(resultado.getId());
        assertEquals(EstadoProducto.DISPONIBLE, resultado.getEstado());
        verify(productoRepo).save(any());
    }

    // =====================================================
    // CREAR PRODUCTO → CATEGORÍA NO EXISTE
    // =====================================================
    @Test
    void crearProducto_categoriaNoExiste_error() {

        ProductoDto dto = new ProductoDto();
        dto.setCategoriaId(99L);

        when(categoriaRepo.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productoService.crear(dto)
        );

        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    // =====================================================
    // LISTAR PRODUCTOS
    // =====================================================
    @Test
    void listarProductos_ok() {

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Collar")
                .estado(EstadoProducto.DISPONIBLE)
                .build();

        when(productoRepo.findAll())
                .thenReturn(List.of(producto));

        List<Producto> lista = productoService.listar();

        assertEquals(1, lista.size());
    }

    // =====================================================
    // OBTENER POR ID → OK
    // =====================================================
    @Test
    void obtenerProductoPorId_ok() {

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Collar")
                .build();

        when(productoRepo.findById(1L))
                .thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerPorId(1L);

        assertEquals("Collar", resultado.getNombre());
    }

    // =====================================================
    // OBTENER POR ID → NO EXISTE
    // =====================================================
    @Test
    void obtenerProductoPorId_noExiste_error() {

        when(productoRepo.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productoService.obtenerPorId(1L)
        );

        assertEquals("Producto no encontrado", ex.getMessage());
    }

    // =====================================================
    // ACTUALIZAR PRODUCTO → OK
    // =====================================================
    @Test
    void actualizarProducto_ok() {

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("Accesorios")
                .build();

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Viejo")
                .stock(5)
                .estado(EstadoProducto.DISPONIBLE)
                .categoria(categoria)
                .build();

        ProductoDto dto = new ProductoDto();
        dto.setNombre("Nuevo");
        dto.setPrecio(8000.0);
        dto.setDescripcion("Nuevo desc");
        dto.setCategoriaId(1L);
        dto.setStock(0);
        dto.setImagen("img");

        when(productoRepo.findById(1L))
                .thenReturn(Optional.of(producto));

        when(categoriaRepo.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(productoRepo.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        ProductoDto actualizado = productoService.actualizar(1L, dto);

        assertEquals(EstadoProducto.SIN_STOCK, actualizado.getEstado());
        verify(productoRepo).save(producto);
    }

    // =====================================================
    // ELIMINAR PRODUCTO
    // =====================================================
    @Test
    void eliminarProducto_ok() {

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Collar")
                .build();

        when(productoRepo.findById(1L))
                .thenReturn(Optional.of(producto));

        productoService.eliminar(1L);

        verify(productoRepo).delete(producto);
    }

    // =====================================================
    // DESCONTAR STOCK → OK
    // =====================================================
    @Test
    void descontarStock_ok() {

        Producto producto = Producto.builder()
                .id(1L)
                .stock(5)
                .estado(EstadoProducto.DISPONIBLE)
                .build();

        when(productoRepo.findById(1L))
                .thenReturn(Optional.of(producto));

        productoService.descontarStock(1L, 3);

        assertEquals(2, producto.getStock());
        verify(productoRepo).save(producto);
    }

    // =====================================================
    // DESCONTAR STOCK → STOCK INSUFICIENTE
    // =====================================================
    @Test
    void descontarStock_insuficiente_error() {

        Producto producto = Producto.builder()
                .id(1L)
                .stock(1)
                .build();

        when(productoRepo.findById(1L))
                .thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productoService.descontarStock(1L, 5)
        );

        assertEquals("Stock insuficiente para completar la compra", ex.getMessage());
    }

    // =====================================================
    // DESCONTAR STOCK → SIN STOCK
    // =====================================================
    @Test
    void descontarStock_sinStock_error() {

        Producto producto = Producto.builder()
                .id(1L)
                .stock(0)
                .build();

        when(productoRepo.findById(1L))
                .thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productoService.descontarStock(1L, 1)
        );

        assertEquals("Producto sin stock disponible", ex.getMessage());
    }
}
