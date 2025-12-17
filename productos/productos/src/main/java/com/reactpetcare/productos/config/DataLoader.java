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
    public void run(String... args) {

        List<Categoria> categoriasExistentes = categoriaRepository.findAll();
        List<Producto> productosExistentes = productoRepository.findAll();

        Categoria accesorios = buscarOCrearCategoria(categoriasExistentes, "Accesorios");
        Categoria juguetes   = buscarOCrearCategoria(categoriasExistentes, "Juguetes");
        Categoria alimentos  = buscarOCrearCategoria(categoriasExistentes, "Alimentos");
        Categoria higiene    = buscarOCrearCategoria(categoriasExistentes, "Higiene");

        // ================= PRODUCTOS EXISTENTES =================
        guardarOActualizar(productosExistentes, "Arnés para perro", 6990.0,
                "Arnés cómodo y ajustable con diseño clásico.",
                accesorios, 10, "images/arnes.jpg");

        guardarOActualizar(productosExistentes, "Cama acolchada para perro", 14990.0,
                "Cama ultrasuave con textura tipo waffle.",
                accesorios, 8, "images/cama.jpg");

        guardarOActualizar(productosExistentes, "Rascador y torre para gatos", 25990.0,
                "Centro de juegos con varias plataformas.",
                juguetes, 6, "images/rascador.jpg");

        guardarOActualizar(productosExistentes, "Collar tropical para perro", 4990.0,
                "Collar resistente con estampado tropical.",
                accesorios, 10, "images/collar.jpg");

        guardarOActualizar(productosExistentes, "Alimento para gato Pro Plan", 18990.0,
                "Nutrición completa con prebióticos naturales.",
                alimentos, 10, "images/alimento_gato.jpg");

        guardarOActualizar(productosExistentes, "Correa retráctil automática", 3990.0,
                "Correa extensible hasta 3 metros.",
                accesorios, 12, "images/correa.jpg");

        guardarOActualizar(productosExistentes, "Juguetes dentales para perro (pack x4)", 7990.0,
                "Pack de juguetes de goma con texturas.",
                juguetes, 14, "images/juguete.jpg");

        guardarOActualizar(productosExistentes, "Plato doble elevado para mascota", 8990.0,
                "Plato doble de cerámica con base de madera.",
                accesorios, 9, "images/plato.jpg");

        guardarOActualizar(productosExistentes, "Shampoo para perros con ceramidas", 6490.0,
                "Shampoo hipoalergénico para brillo y suavidad.",
                higiene, 10, "images/shampo.jpg");

        // ================= PRODUCTOS NUEVOS =================

        // 🐶 ACCESORIOS
        guardarOActualizar(productosExistentes, "Collar rojo para perro", 3990.0,
                "Collar de nylon resistente con hebilla de seguridad.",
                accesorios, 20, "images/collar_rojo.png");

        guardarOActualizar(productosExistentes, "Correa retráctil azul 5m", 12990.0,
                "Correa retráctil para perros medianos hasta 15kg.",
                accesorios, 15, "images/correa_retractilazul.png");

        guardarOActualizar(productosExistentes, "Plato doble plástico con bowls de acero", 9990.0,
                "Plato doble con bowls de acero inoxidable.",
                accesorios, 12, "images/plato_doble.png");

        // 🍖 ALIMENTOS
        guardarOActualizar(productosExistentes, "Dog Chow Adultos Minis y Pequeños 3kg", 15990.0,
                "Alimento seco para perros adultos de razas pequeñas.",
                alimentos, 18, "images/comida_perrodogchow.png");

        guardarOActualizar(productosExistentes, "Snack dental Pedigree Dentastix", 4990.0,
                "Snack dental para cuidado oral diario.",
                alimentos, 30, "images/snack_dentalpedigree.png");

        // 🧼 HIGIENE
        guardarOActualizar(productosExistentes, "Corta uñas para mascotas", 5990.0,
                "Corta uñas de acero inoxidable para perros y gatos.",
                higiene, 25, "images/cortaunias.png");

        guardarOActualizar(productosExistentes, "Shampoo para gatos", 6490.0,
                "Shampoo suave que fortalece y da brillo al pelaje.",
                higiene, 20, "images/shampoo_gato.png");

        guardarOActualizar(productosExistentes, "Toallitas húmedas Pet Care", 5490.0,
                "Toallitas húmedas de aseo para mascotas (50 unidades).",
                higiene, 22, "images/toallitas_petclean.png");

        // 🧸 JUGUETES
        guardarOActualizar(productosExistentes, "Cuerda mordedora para perro", 4490.0,
                "Juguete de cuerda resistente para juegos interactivos.",
                juguetes, 18, "images/cuerda_mordedora.png");

        guardarOActualizar(productosExistentes, "Juguete de goma interactivo", 6990.0,
                "Juguete de goma resistente para perros.",
                juguetes, 14, "images/juguete_goma.png");

        guardarOActualizar(productosExistentes, "Ratones de tela para gato (pack)", 5990.0,
                "Pack de ratones de tela con cuerda para gatos.",
                juguetes, 16, "images/raton_tela.png");

        System.out.println(">>> Productos verificados y actualizados correctamente ✔️");
    }

    // =========================================================

    private Categoria buscarOCrearCategoria(List<Categoria> existentes, String nombre) {
        return existentes.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombre)));
    }

    private void guardarOActualizar(
            List<Producto> existentes,
            String nombre,
            Double precio,
            String descripcion,
            Categoria categoria,
            Integer stock,
            String imagePath
    ) {
        String imagenBase64 = cargarImagen(imagePath);

        Optional<Producto> existente = existentes.stream()
                .filter(p -> p.getNombre().equals(nombre))
                .findFirst();

        if (existente.isPresent()) {
            Producto p = existente.get();
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

            ClassPathResource imgFile = new ClassPathResource(path);
            if (imgFile.exists()) {
                bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
            } else {
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
            return "data:image/" + extension + ";base64," +
                    Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
