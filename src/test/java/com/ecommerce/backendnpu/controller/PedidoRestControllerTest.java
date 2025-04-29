package com.ecommerce.backendnpu.controller;


import com.ecommerce.backendnpu.Api.PedidoRestController;
import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.service.PedidoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PedidoRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PedidoRestController pedidoRestController;

    private Rol rolComprador;
    private Rol rolVendedor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoRestController).build();

        rolComprador = new Rol();
        rolComprador.setNombre(ERol.ROLE_COMPRADOR);

        rolVendedor = new Rol();
        rolVendedor.setNombre(ERol.ROLE_VENDEDOR);
    }

    private void mockAuthentication(String email, ERol rol) {
        Rol userRol = new Rol();
        userRol.setNombre(rol);

        Usuario usuario = new Usuario();
        usuario.setCorreo(email);
        usuario.setRol(userRol);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                Collections.singletonList(() -> "ROLE_" + rol.name())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getPedidosDelComprador_Success() throws Exception {
        // Configurar
        Usuario comprador = new Usuario();
        comprador.setCorreo("comprador@test.com");
        comprador.setRol(rolComprador);

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.of(comprador));
        when(pedidoService.findByComprador(comprador)).thenReturn(List.of(pedido));
        mockAuthentication("comprador@test.com", ERol.ROLE_COMPRADOR);

        // Ejecutar y Verificar
        mockMvc.perform(get("/api/pedidos/mis-pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(pedidoService).findByComprador(comprador);
    }

    @Test
    void getVentasDelVendedor_Success() throws Exception {
        // Configurar
        Usuario vendedor = new Usuario();
        vendedor.setCorreo("vendedor@test.com");
        vendedor.setRol(rolVendedor);

        PedidoItem item = new PedidoItem();
        item.setId(1L);

        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.of(vendedor));
        when(pedidoService.findPedidoItemsByVendedor(vendedor)).thenReturn(List.of(item));
        mockAuthentication("vendedor@test.com", ERol.ROLE_VENDEDOR);

        // Ejecutar y Verificar
        mockMvc.perform(get("/api/pedidos/mis-ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(pedidoService).findPedidoItemsByVendedor(vendedor);
    }

    @Test
    void getPedidosDelComprador_UserNotFound_ShouldThrow() throws Exception {
        mockAuthentication("comprador@test.com", ERol.ROLE_COMPRADOR);
        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedidos/mis-pedidos"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getPedidosDelComprador_WrongRole_ShouldForbid() throws Exception {
        mockAuthentication("vendedor@test.com", ERol.ROLE_VENDEDOR);

        Usuario usuario = new Usuario();
        usuario.setRol(rolVendedor);
        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/pedidos/mis-pedidos"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getVentasDelVendedor_UnauthorizedRole_ShouldForbid() throws Exception {
        mockAuthentication("comprador@test.com", ERol.ROLE_COMPRADOR);

        Usuario usuario = new Usuario();
        usuario.setRol(rolComprador);
        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/pedidos/mis-ventas"))
                .andExpect(status().isForbidden());
    }
}