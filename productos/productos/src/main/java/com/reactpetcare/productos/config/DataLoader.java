package com.reactpetcare.productos.config;

import com.reactpetcare.productos.model.Categoria;
import com.reactpetcare.productos.model.EstadoProducto;
import com.reactpetcare.productos.model.Producto;
import com.reactpetcare.productos.repository.CategoriaRepository;
import com.reactpetcare.productos.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. Cargamos listas en memoria para verificar qué existe y qué no
        List<Categoria> categoriasExistentes = categoriaRepository.findAll();
        List<Producto> productosExistentes = productoRepository.findAll();

        // 2. Categorías (Busca si existe, si no, la crea)
        Categoria accesorios = buscarOCrearCategoria(categoriasExistentes, "Accesorios");
        Categoria juguetes   = buscarOCrearCategoria(categoriasExistentes, "Juguetes");
        Categoria alimentos  = buscarOCrearCategoria(categoriasExistentes, "Alimentos");
        Categoria higiene    = buscarOCrearCategoria(categoriasExistentes, "Higiene");

        // 3. Productos (Crea o actualiza la imagen si ya existe)
        guardarOActualizar(productosExistentes, "Arnés para perro", 6990.0, "Arnés cómodo y ajustable con diseño clásico.", accesorios, 10, "images/arnes.jpg");
        guardarOActualizar(productosExistentes, "Cama acolchada para perro", 14990.0, "Cama ultrasuave con textura tipo waffle.", accesorios, 8, "images/cama.jpg");
        guardarOActualizar(productosExistentes, "Rascador y torre para gatos", 25990.0, "Centro de juegos con varias plataformas.", juguetes, 6, "images/rascador.jpg");
        guardarOActualizar(productosExistentes, "Collar tropical para perro", 4990.0, "Collar resistente con estampado tropical.", accesorios, 10, "images/collar.jpg");
        guardarOActualizar(productosExistentes, "Alimento para gato Pro Plan", 18990.0, "Nutrición completa con prebióticos naturales.", alimentos, 10, "images/alimento_gato.jpg");
        guardarOActualizar(productosExistentes, "Correa retráctil automática", 3990.0, "Correa extensible hasta 3 metros.", accesorios, 12, "images/correa.jpg");
        guardarOActualizar(productosExistentes, "Juguetes dentales para perro (pack x4)", 7990.0, "Pack de juguetes de goma con texturas.", juguetes, 14, "images/juguete.jpg");
        guardarOActualizar(productosExistentes, "Plato doble elevado para mascota", 8990.0, "Plato doble de cerámica con base de madera.", accesorios, 9, "images/plato.jpg");
        guardarOActualizar(productosExistentes, "Shampoo para perros con ceramidas", 6490.0, "Shampoo hipoalergénico para brillo y suavidad.", higiene, 10, "images/shampo.jpg");

        System.out.println(">>> Productos verificados y actualizados correctamente ✔️");
    }

    private Categoria buscarOCrearCategoria(List<Categoria> existentes, String nombre) {
        return existentes.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombre)));
    }

    private void guardarOActualizar(List<Producto> existentes, String nombre, Double precio, String descripcion, Categoria categoria, Integer stock, String imagePath) {
        String imagenBase64 = cargarImagen(imagePath);
        
        Optional<Producto> existente = existentes.stream()
                .filter(p -> p.getNombre().equals(nombre))
                .findFirst();

        if (existente.isPresent()) {
            Producto p = existente.get();
            // Si encontramos la imagen en la carpeta, actualizamos el producto existente
            if (imagenBase64 != null) {
                p.setImagen(imagenBase64);
                productoRepository.save(p);
            }
        } else {
            productoRepository.save(Producto.builder()
                    .nombre(nombre)
                    .precio(precio)
                    .descripcion(descripcion)
                    .categoria(categoria)
                    .stock(stock)
                    .estado(EstadoProducto.DISPONIBLE)
                    .imagen(imagenBase64)
                    .build());
        }
    }

    private String cargarImagen(String path) {
        try {
            byte[] bytes = null;

            // 1. Intentar cargar desde el classpath (compilado)
            ClassPathResource imgFile = new ClassPathResource(path);
            if (imgFile.exists()) {
                bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
            } else {
                // 2. Fallback: Intentar cargar desde la carpeta src (desarrollo local sin rebuild)
                File file = new File("src/main/resources/" + path);
                if (file.exists()) {
                    bytes = Files.readAllBytes(file.toPath());
                }
            }

            if (bytes == null) {
                System.out.println(">>> ADVERTENCIA: No se encontró la imagen: " + path);
                return null;
            }
            String extension = path.endsWith(".png") ? "png" : "jpeg";
            return "data:image/" + extension + ";base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
