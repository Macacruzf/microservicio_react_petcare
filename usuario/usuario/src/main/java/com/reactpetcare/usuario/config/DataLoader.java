package com.reactpetcare.usuario.config;

import com.reactpetcare.usuario.model.RolUsuario;
import com.reactpetcare.usuario.model.Usuario;
import com.reactpetcare.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUsuarios(UsuarioRepository repo) {
        return args -> {

            if (repo.count() == 0) {

                Usuario admin = Usuario.builder()
                        .nombre("Administrador")
                        .email("admin@petcare.cl")
                        .telefono("999999999")
                        .password(passwordEncoder.encode("Admin.123"))
                        .rol(RolUsuario.ADMIN)
                        .build();

                Usuario cliente1 = Usuario.builder()
                        .nombre("Cliente Uno")
                        .email("cliente@petcare.cl")
                        .telefono("912345678")
                        .password(passwordEncoder.encode("Cliente.123"))
                        .rol(RolUsuario.CLIENTE)
                        .build();

                Usuario cliente2 = Usuario.builder()
                        .nombre("Cliente Dos")
                        .email("cliente2@petcare.cl")
                        .telefono("923456789")
                        .password(passwordEncoder.encode("Cliente2.123"))
                        .rol(RolUsuario.CLIENTE)
                        .build();

                repo.save(admin);
                repo.save(cliente1);
                repo.save(cliente2);

                System.out.println(">>> Usuarios iniciales cargados con éxito");
            } else {
                System.out.println(">>> Usuarios ya cargados. Se omite inicialización.");
            }
        };
    }
}
