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
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .password(passwordEncoder.encode("123456")) // contraseña por defecto
                .rol(dto.getRol() != null && !dto.getRol().isEmpty() ? RolUsuario.valueOf(dto.getRol()) : RolUsuario.CLIENTE)
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
                .apellido(req.getApellido())
                .email(req.getEmail())
                .direccion(req.getDireccion())
                .telefono(req.getTelefono())
                .password(passwordEncoder.encode(req.getPassword()))
                .rol(req.getRol() != null && !req.getRol().isEmpty() ? RolUsuario.valueOf(req.getRol()) : RolUsuario.CLIENTE)
                .build();

        usuarioRepositorio.save(usuario);

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  LOGIN (sin JWT, solo validación)
    // ---------------------------------------------------------
    public LoginResponse login(LoginRequest req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        Usuario usuario = usuarioRepositorio.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new LoginResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name()
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
        usuario.setApellido(dto.getApellido());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());
        if (dto.getRol() != null) {
            usuario.setRol(RolUsuario.valueOf(dto.getRol()));
        }

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
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setDireccion(usuario.getDireccion());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol().name());
        return dto;
    }
}
