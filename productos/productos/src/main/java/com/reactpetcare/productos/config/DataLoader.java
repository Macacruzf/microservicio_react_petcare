package com.reactpetcare.productos.config;

import com.reactpetcare.productos.model.Categoria;
import com.reactpetcare.productos.model.EstadoProducto;
import com.reactpetcare.productos.model.Producto;
import com.reactpetcare.productos.repository.CategoriaRepository;
import com.reactpetcare.productos.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {

        if (productoRepository.count() > 0) return;

        // Categorías automáticas
        Categoria accesorios = categoriaRepository.save(new Categoria(null, "Accesorios"));
        Categoria juguetes    = categoriaRepository.save(new Categoria(null, "Juguetes"));
        Categoria alimentos   = categoriaRepository.save(new Categoria(null, "Alimentos"));
        Categoria higiene     = categoriaRepository.save(new Categoria(null, "Higiene"));

        // Productos iniciales
        productoRepository.save(Producto.builder()
                .nombre("Arnés para perro")
                .precio(12990.0)
                .descripcion("Arnés cómodo y ajustable con diseño clásico.")
                .categoria(accesorios)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Cama acolchada para perro")
                .precio(34990.0)
                .descripcion("Cama ultrasuave con textura tipo waffle.")
                .categoria(accesorios)
                .stock(8)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Rascador y torre para gatos")
                .precio(65990.0)
                .descripcion("Centro de juegos con varias plataformas.")
                .categoria(juguetes)
                .stock(6)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Collar tropical para perro")
                .precio(8990.0)
                .descripcion("Collar resistente con estampado tropical.")
                .categoria(accesorios)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Alimento para gato Pro Plan")
                .precio(28990.0)
                .descripcion("Nutrición completa con prebióticos naturales.")
                .categoria(alimentos)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Correa retráctil automática")
                .precio(10990.0)
                .descripcion("Correa extensible hasta 3 metros.")
                .categoria(accesorios)
                .stock(12)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Juguetes dentales para perro (pack x4)")
                .precio(9990.0)
                .descripcion("Pack de juguetes de goma con texturas.")
                .categoria(juguetes)
                .stock(14)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Plato doble elevado para mascota")
                .precio(14990.0)
                .descripcion("Plato doble de cerámica con base de madera.")
                .categoria(accesorios)
                .stock(9)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Shampoo para perros con ceramidas")
                .precio(8490.0)
                .descripcion("Shampoo hipoalergénico para brillo y suavidad.")
                .categoria(higiene)
                .stock(10)
                .estado(EstadoProducto.DISPONIBLE)
                .build());

        System.out.println("Productos iniciales cargados correctamente ✔️");
    }
}
