package com.reactpetcare.usuario.service;

import com.reactpetcare.usuario.dto.UsuarioDto;
import com.reactpetcare.usuario.dto.RegistroRequest;
import com.reactpetcare.usuario.dto.LoginRequest;
import com.reactpetcare.usuario.dto.LoginResponse;

import com.reactpetcare.usuario.model.RolUsuario;
import com.reactpetcare.usuario.model.Usuario;
import com.reactpetcare.usuario.repository.UsuarioRepository;
import com.reactpetcare.usuario.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    // ---------------------------------------------------------
    //  Registrar usuario desde UsuarioDto (para pruebas internas)
    // ---------------------------------------------------------
    public UsuarioDto crear(UsuarioDto dto) {

        if (usuarioRepositorio.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .password(passwordEncoder.encode("123456")) // contraseña por defecto
                .rol(dto.getRol() != null ? dto.getRol() : RolUsuario.CLIENTE)
                .build();

        usuarioRepositorio.save(usuario);

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  Registro completo (formulario)
    // ---------------------------------------------------------
    public UsuarioDto registrar(RegistroRequest req) {

        if (usuarioRepositorio.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(req.getNombre())
                .email(req.getEmail())
                .telefono(req.getTelefono())
                .password(passwordEncoder.encode(req.getPassword()))
                .rol(req.getRol() != null ? req.getRol() : RolUsuario.CLIENTE)
                .build();

        usuarioRepositorio.save(usuario);

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  LOGIN
    // ---------------------------------------------------------
    public LoginResponse login(LoginRequest req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        Usuario usuario = usuarioRepositorio.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generarToken(usuario.getEmail());

        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    // ---------------------------------------------------------
    //  OBTENER POR ID
    // ---------------------------------------------------------
    public UsuarioDto obtenerPorId(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  LISTAR TODOS → ahora devuelve DTOs
    // ---------------------------------------------------------
    public List<UsuarioDto> listar() {
        return usuarioRepositorio.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    //  ACTUALIZAR
    // ---------------------------------------------------------
    public UsuarioDto actualizar(Long id, UsuarioDto dto) {

        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());

        usuarioRepositorio.save(usuario);

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  Conversión de Entidad → DTO
    // ---------------------------------------------------------
    private UsuarioDto mapToDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        return dto;
    }
}
