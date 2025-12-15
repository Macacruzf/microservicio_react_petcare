package com.reactpetcare.usuario.service;

import com.reactpetcare.usuario.dto.*;
import com.reactpetcare.usuario.model.*;
import com.reactpetcare.usuario.repository.*;
import com.reactpetcare.usuario.security.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepositorio;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    // =====================================================
    // CREAR (UsuarioDto)
    // =====================================================
    @Test
    void crearUsuario_ok() {

        UsuarioDto dto = new UsuarioDto();
        dto.setUsername("francisca");
        dto.setRol("CLIENTE");

        Rol rol = Rol.builder()
                .id(1L)
                .nombre(RolNombre.CLIENTE)
                .build();

        when(usuarioRepositorio.findByUsername("francisca"))
                .thenReturn(Optional.empty());

        when(rolRepository.findByNombre(RolNombre.CLIENTE))
                .thenReturn(Optional.of(rol));

        when(passwordEncoder.encode(any()))
                .thenReturn("password-encriptada");

        when(usuarioRepositorio.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioDto resultado = usuarioService.crear(dto);

        assertNotNull(resultado);
        verify(usuarioRepositorio).save(any());
    }

    // =====================================================
    // REGISTRAR
    // =====================================================
    @Test
    void registrarUsuario_ok() {

        RegistroRequest req = new RegistroRequest();
        req.setNombre("Francisca");
        req.setUsername("francisca");
        req.setPassword("1234");

        Rol rol = Rol.builder()
                .id(1L)
                .nombre(RolNombre.CLIENTE)
                .build();

        when(usuarioRepositorio.findByUsername("francisca"))
                .thenReturn(Optional.empty());

        when(rolRepository.findByNombre(RolNombre.CLIENTE))
                .thenReturn(Optional.of(rol));

        when(passwordEncoder.encode("1234"))
                .thenReturn("password-encriptada");

        when(usuarioRepositorio.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioDto resultado = usuarioService.registrar(req);

        assertEquals("Francisca", resultado.getNombre());
    }

    // =====================================================
    // LOGIN
    // =====================================================
    @Test
    void login_ok() {

        LoginRequest req = new LoginRequest();
        req.setEmail("francisca@mail.com");
        req.setPassword("1234");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Francisca")
                .apellido("Castro")
                .email("francisca@mail.com")
                .roles(Set.of(
                        Rol.builder()
                                .nombre(RolNombre.CLIENTE)
                                .build()
                ))
                .build();

        when(usuarioRepositorio.findByEmail("francisca@mail.com"))
                .thenReturn(Optional.of(usuario));

        when(jwtUtil.generarToken("francisca@mail.com"))
                .thenReturn("jwt-token");

        LoginResponse response = usuarioService.login(req);

        assertEquals("jwt-token", response.getToken());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @Test
    void obtenerUsuarioPorId_ok() {

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Francisca")
                .roles(Set.of(
                        Rol.builder().nombre(RolNombre.CLIENTE).build()
                ))
                .build();

        when(usuarioRepositorio.findById(1L))
                .thenReturn(Optional.of(usuario));

        UsuarioDto dto = usuarioService.obtenerPorId(1L);

        assertEquals(1L, dto.getId());
    }

    // =====================================================
    // LISTAR
    // =====================================================
    @Test
    void listarUsuarios_ok() {

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Francisca")
                .roles(Set.of(
                        Rol.builder().nombre(RolNombre.CLIENTE).build()
                ))
                .build();

        when(usuarioRepositorio.findAll())
                .thenReturn(List.of(usuario));

        List<UsuarioDto> lista = usuarioService.listar();

        assertEquals(1, lista.size());
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================
    @Test
    void actualizarUsuario_ok() {

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Viejo")
                .roles(Set.of(
                        Rol.builder().nombre(RolNombre.CLIENTE).build()
                ))
                .build();

        UsuarioDto dto = new UsuarioDto();
        dto.setNombre("Nuevo");
        dto.setRol("CLIENTE");

        when(usuarioRepositorio.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(rolRepository.findByNombre(RolNombre.CLIENTE))
                .thenReturn(Optional.of(
                        Rol.builder().nombre(RolNombre.CLIENTE).build()
                ));

        when(usuarioRepositorio.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioDto actualizado = usuarioService.actualizar(1L, dto);

        assertEquals("Nuevo", actualizado.getNombre());
    }
}
