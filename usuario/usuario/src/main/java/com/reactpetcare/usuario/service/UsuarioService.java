package com.reactpetcare.usuario.service;

import com.reactpetcare.usuario.dto.UsuarioDto;
import com.reactpetcare.usuario.dto.RegistroRequest;
import com.reactpetcare.usuario.dto.LoginRequest;
import com.reactpetcare.usuario.dto.LoginResponse;
import com.reactpetcare.usuario.model.RolNombre;
import com.reactpetcare.usuario.model.Rol;
import com.reactpetcare.usuario.model.Usuario;
import com.reactpetcare.usuario.repository.RolRepository;
import com.reactpetcare.usuario.repository.UsuarioRepository;
import com.reactpetcare.usuario.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepositorio;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    // ---------------------------------------------------------
    //  Registrar usuario desde UsuarioDto (para pruebas internas)
    // ---------------------------------------------------------
    public UsuarioDto crear(UsuarioDto dto) {

        if (usuarioRepositorio.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Buscamos el rol en la BD o lanzamos error
        String rolNombre = dto.getRol() != null ? dto.getRol() : "CLIENTE";
        Rol rol = rolRepository.findByNombre(RolNombre.valueOf(rolNombre))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .username(dto.getUsername()) // Faltaba asignar el username
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .password(passwordEncoder.encode("123456")) // contraseña por defecto
                .roles(Set.of(rol)) // Asignamos el Set de roles
                .build();

        try {
            usuarioRepositorio.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("El email ya está registrado");
        }

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  Registro completo (formulario)
    // ---------------------------------------------------------
    public UsuarioDto registrar(RegistroRequest req) {

        if (usuarioRepositorio.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está ocupado");
        }

        Rol rol = rolRepository.findByNombre(RolNombre.CLIENTE)
                .orElseThrow(() -> new RuntimeException("Error al asignar rol por defecto"));

        Usuario usuario = Usuario.builder()
                .nombre(req.getNombre())
                .apellido(req.getApellido())
                .username(req.getUsername())
                .email(req.getEmail())
                .direccion(req.getDireccion())
                .telefono(req.getTelefono())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(rol))
                .build();

        try {
            usuarioRepositorio.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("El email ya está registrado");
        }

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  LOGIN (sin JWT, solo validación)
    // ---------------------------------------------------------
    public LoginResponse login(LoginRequest req) {

        // Autenticamos usando email
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        Usuario usuario = usuarioRepositorio.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generarToken(usuario.getEmail());

        return new LoginResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRoles().iterator().next().getNombre().name(), // Obtenemos el nombre del primer rol
                token // Asegúrate de agregar este campo a tu clase LoginResponse
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
            Rol rol = rolRepository.findByNombre(RolNombre.valueOf(dto.getRol())).orElseThrow();
            usuario.setRoles(Set.of(rol));
        }

        usuarioRepositorio.save(usuario);

        return mapToDto(usuario);
    }

    // ---------------------------------------------------------
    //  CAMBIAR CONTRASEÑA
    // ---------------------------------------------------------
    @Transactional
    public void cambiarPassword(Long id, com.reactpetcare.usuario.dto.CambiarPasswordRequest request) {
        
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Actualizar con la nueva contraseña encriptada
        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        usuarioRepositorio.saveAndFlush(usuario);
        
        System.out.println("✅ Contraseña actualizada para usuario ID: " + id);
        System.out.println("✅ Nueva contraseña encriptada: " + usuario.getPassword().substring(0, 20) + "...");
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
        dto.setUsername(usuario.getUsername());
        // Mapeamos el primer rol encontrado a String
        dto.setRol(usuario.getRoles().stream().findFirst().map(r -> r.getNombre().name()).orElse("CLIENTE"));
        return dto;
    }
}
