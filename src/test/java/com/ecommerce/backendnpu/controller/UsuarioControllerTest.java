package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.UsuarioRestController;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioRestController(usuarioService))
                .build();
    }

    @Test
    void crearUsuarioSuccess() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Mockito.when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUsuario\":\"test\", \"correo\":\"test@test.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void obtenerUsuarioNoExistente() throws Exception {
        Mockito.when(usuarioService.findUsuarioById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarUsuarioRolInvalido() throws Exception {
        Mockito.when(usuarioService.registrarNuevoUsuario(any(), any()))
                .thenThrow(new RuntimeException("Rol no encontrado"));

        mockMvc.perform(post("/api/usuarios/registro?cargoRol=INVALIDO")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"test@test.com\", \"contrasena\":\"pass\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarUsuarioConflict() throws Exception {
        Mockito.when(usuarioService.saveUsuario(any()))
                .thenThrow(new RuntimeException("Error de actualización"));

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUsuario\":\"nuevo\"}"))
                .andExpect(status().isBadRequest());
    }
}