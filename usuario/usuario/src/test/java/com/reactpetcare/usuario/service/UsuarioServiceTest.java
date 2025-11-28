package com.reactpetcare.usuario.service;

import com.reactpetcare.usuario.dto.*;
import com.reactpetcare.usuario.model.RolUsuario;
import com.reactpetcare.usuario.model.Usuario;
import com.reactpetcare.usuario.repository.UsuarioRepository;
import com.reactpetcare.usuario.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepositorio;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil; // No se usa pero forma parte del constructor

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // TEST REGISTRO COMPLETO (formulario)
    // ---------------------------------------------------------
    @Test
    void registrar_debeCrearUsuario() {
        RegistroRequest req = new RegistroRequest();
        req.setNombre("Francisca");
        req.setApellido("González");
        req.setEmail("test@test.com");
        req.setDireccion("Calle 1");
        req.setTelefono("123456");
        req.setPassword("1234");
        req.setRol("CLIENTE");

        when(usuarioRepositorio.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("pass-encriptada");

        Usuario usuarioGuardado = Usuario.builder()
                .id(1L)
                .nombre("Francisca")
                .apellido("González")
                .email("test@test.com")
                .direccion("Calle 1")
                .telefono("123456")
                .password("pass-encriptada")
                .rol(RolUsuario.CLIENTE)
                .build();

        when(usuarioRepositorio.save(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        UsuarioDto response = usuarioService.registrar(req);

        assertEquals("Francisca", response.getNombre());
        assertEquals("González", response.getApellido());
        assertEquals("test@test.com", response.getEmail());
        assertEquals("CLIENTE", response.getRol());
    }


    // ---------------------------------------------------------
    // TEST OBTENER POR ID
    // ---------------------------------------------------------
    @Test
    void obtenerPorId_debeRetornarUsuario() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Fran")
                .apellido("G")
                .email("fran@test.com")
                .rol(RolUsuario.CLIENTE)
                .build();

        when(usuarioRepositorio.findById(1L))
                .thenReturn(Optional.of(usuario));

        UsuarioDto dto = usuarioService.obtenerPorId(1L);

        assertEquals("Fran", dto.getNombre());
        assertEquals("G", dto.getApellido());
        assertEquals("fran@test.com", dto.getEmail());
    }

    // ---------------------------------------------------------
    // TEST LISTAR
    // ---------------------------------------------------------
    @Test
    void listar_debeRetornarListaDeUsuarios() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Francisca")
                .apellido("González")
                .email("fran@test.com")
                .rol(RolUsuario.CLIENTE)
                .build();

        when(usuarioRepositorio.findAll())
                .thenReturn(List.of(usuario));

        List<UsuarioDto> lista = usuarioService.listar();

        assertEquals(1, lista.size());
        assertEquals("Francisca", lista.get(0).getNombre());
    }

    // ---------------------------------------------------------
    // TEST ACTUALIZAR
    // ---------------------------------------------------------
    @Test
    void actualizar_debeModificarUsuario() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Fran")
                .apellido("G")
                .direccion("Dir 1")
                .telefono("111")
                .rol(RolUsuario.CLIENTE)
                .build();

        UsuarioDto cambios = new UsuarioDto();
        cambios.setNombre("Nuevo Nombre");
        cambios.setApellido("Nuevo Apellido");
        cambios.setDireccion("Nueva Dir");
        cambios.setTelefono("222");
        cambios.setRol("CLIENTE");

        when(usuarioRepositorio.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepositorio.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDto resultado = usuarioService.actualizar(1L, cambios);

        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals("Nuevo Apellido", resultado.getApellido());
        assertEquals("Nueva Dir", resultado.getDireccion());
        assertEquals("222", resultado.getTelefono());
        assertEquals("CLIENTE", resultado.getRol());
    }

    // ---------------------------------------------------------
    // TEST LOGIN — SIN AUTHENTICATION Y SIN TOKEN
    // ---------------------------------------------------------
    @Test
    void login_retornaDatosUsuarioCorrectos() {

        // REQUEST
        LoginRequest request = new LoginRequest();
        request.setEmail("francisca@example.com");
        request.setPassword("123456");

        // USUARIO SIMULADO
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Francisca")
                .apellido("Castro")
                .email("francisca@example.com")
                .rol(RolUsuario.CLIENTE)
                .build();

        // MOCK: cuando se busca por email → retorna usuario
        when(usuarioRepositorio.findByEmail("francisca@example.com"))
                .thenReturn(Optional.of(usuario));

        // ACT
        LoginResponse resp = usuarioService.login(request);

        // ASSERT
        assertNotNull(resp);
        assertEquals(1L, resp.getUserId());
        assertEquals("Francisca", resp.getNombre());
        assertEquals("Castro", resp.getApellido());
        assertEquals("francisca@example.com", resp.getEmail());
        assertEquals("CLIENTE", resp.getRol());

        verify(usuarioRepositorio, times(1)).findByEmail("francisca@example.com");
    }
}


