package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.UsuarioRestController;
import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioRestController usuarioRestController;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioRestController).build();

        Rol rolUsuario = new Rol();
        rolUsuario.setId(Rol.ID_COMPRADOR); // Usando la constante definida en Rol
        rolUsuario.setNombre("CLIENTE"); // Usando nombre en lugar de cargo
        rolUsuario.setDescripcion("Usuario que puede comprar productos"); // Adicional: completando todos los campos

        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setNombre("Juan");
        usuarioEjemplo.setApellido("Pérez");
        usuarioEjemplo.setCorreo("juan.perez@ejemplo.com");
        usuarioEjemplo.setRut("12345678-9");
        usuarioEjemplo.setVerificar(true);
        usuarioEjemplo.setContrasena("password123");
        usuarioEjemplo.setRol(rolUsuario);
    }

    @Test
    void obtenerUsuarioExistente_Devuelve200() throws Exception {
        when(usuarioService.findUsuarioById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        mockMvc.perform(get("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.correo").value("juan.perez@ejemplo.com"));
    }

    @Test
    void obtenerUsuarioNoExistente_Devuelve404() throws Exception {
        when(usuarioService.findUsuarioById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearUsuario_DatosValidos_Devuelve201() throws Exception {
        when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuarioEjemplo);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEjemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void obtenerTodosLosUsuarios_Devuelve200YListaDeUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuarioEjemplo);
        when(usuarioService.findAllUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void actualizarUsuario_UsuarioExistente_Devuelve200() throws Exception {
        when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuarioEjemplo);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEjemplo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void eliminarUsuario_UsuarioExistente_Devuelve204() throws Exception {
        doNothing().when(usuarioService).deleteUsuarioById(anyLong());

        mockMvc.perform(delete("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarUsuarioPorCorreo_UsuarioExistente_Devuelve200() throws Exception {
        when(usuarioService.findUsuarioByCorreo(anyString())).thenReturn(Optional.of(usuarioEjemplo));

        mockMvc.perform(get("/api/usuarios/correo/juan.perez@ejemplo.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.correo").value("juan.perez@ejemplo.com"));
    }

    @Test
    void registrarUsuario_DatosValidos_Devuelve201() throws Exception {
        when(usuarioService.registrarNuevoUsuario(any(Usuario.class), anyString())).thenReturn(usuarioEjemplo);

        mockMvc.perform(post("/api/usuarios/registro")
                        .param("cargoRol", "CLIENTE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEjemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }
}