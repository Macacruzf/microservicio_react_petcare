package com.reactpetcare.usuario.config;

import com.reactpetcare.usuario.model.Rol;
import com.reactpetcare.usuario.model.RolNombre;
import com.reactpetcare.usuario.model.Usuario;
import com.reactpetcare.usuario.repository.RolRepository;
import com.reactpetcare.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    @Bean
    public CommandLineRunner initUsuarios(UsuarioRepository repo) {
        return args -> {

            // 1. Inicializar Roles (Idempotente: crea solo si no existe)
            Rol rolAdmin = rolRepository.findByNombre(RolNombre.ADMIN)
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre(RolNombre.ADMIN).build()));
            Rol rolCliente = rolRepository.findByNombre(RolNombre.CLIENTE)
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre(RolNombre.CLIENTE).build()));

            // 2. Cargar usuarios en memoria para verificar conflictos de email
            List<Usuario> usuariosActuales = repo.findAll();

            // --- USUARIO ADMIN ---
            // Buscamos si ya existe por username O por email
            Optional<Usuario> adminExistente = usuariosActuales.stream()
                    .filter(u -> u.getUsername().equals("admin") || u.getEmail().equals("admin@petcare.cl"))
                    .findFirst();

            if (adminExistente.isEmpty()) {
                repo.save(Usuario.builder()
                        .nombre("Administrador")
                        .apellido("Sistema")
                        .direccion("Oficina Central")
                        .username("admin")
                        .email("admin@petcare.cl")
                        .telefono("999999999")
                        .password(passwordEncoder.encode("Admin.123"))
                        .roles(Set.of(rolAdmin))
                        .build());
                System.out.println(">>> Usuario 'admin' creado.");
            } else {
                // Si existe (quizás con otro username pero mismo email), lo actualizamos para asegurar acceso
                Usuario u = adminExistente.get();
                u.setUsername("admin"); // Forzamos el username correcto
                u.setPassword(passwordEncoder.encode("Admin.123"));
                u.setRoles(Set.of(rolAdmin));
                repo.save(u);
                System.out.println(">>> Usuario 'admin' verificado y actualizado.");
            }

            // --- USUARIO CLIENTE 1 ---
            if (usuariosActuales.stream().noneMatch(u -> u.getUsername().equals("cliente1") || u.getEmail().equals("cliente@petcare.cl"))) {
                Usuario cliente1 = Usuario.builder()
                        .nombre("Cliente Uno")
                        .apellido("Perez")
                        .direccion("Calle 123")
                        .username("cliente1")
                        .email("cliente@petcare.cl")
                        .telefono("912345678")
                        .password(passwordEncoder.encode("Cliente.123"))
                        .roles(Set.of(rolCliente))
                        .build();
                repo.save(cliente1);
                System.out.println(">>> Usuario 'cliente1' creado.");
            }

            // --- USUARIO CLIENTE 2 ---
            if (usuariosActuales.stream().noneMatch(u -> u.getUsername().equals("cliente2") || u.getEmail().equals("cliente2@petcare.cl"))) {
                Usuario cliente2 = Usuario.builder()
                        .nombre("Cliente Dos")
                        .apellido("Gomez")
                        .direccion("Avenida 456")
                        .username("cliente2")
                        .email("cliente2@petcare.cl")
                        .telefono("923456789")
                        .password(passwordEncoder.encode("Cliente2.123"))
                        .roles(Set.of(rolCliente))
                        .build();
                repo.save(cliente2);
                System.out.println(">>> Usuario 'cliente2' creado.");
            }
        };
    }
}
