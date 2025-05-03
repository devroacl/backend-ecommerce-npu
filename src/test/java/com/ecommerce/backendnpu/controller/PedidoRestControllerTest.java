package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.PedidoRestController;
import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.security.JwtAuthenticationFilter;
import com.ecommerce.backendnpu.security.JwtUtils;
import com.ecommerce.backendnpu.security.UserDetailsServiceImpl;
import com.ecommerce.backendnpu.service.PedidoService;

import com.ecommerce.backendnpu.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PedidoRestController.class)
@Import(JwtAuthenticationFilter.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoRestControllerTest {


    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private PedidoService pedidoService; // Interfaz ✅

    @MockitoBean
    private UsuarioService usuarioService; // Interfaz ✅

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PedidoRestController pedidoRestController;

    private Rol rolComprador;
    private Rol rolVendedor;

    @BeforeEach
    void setUp() {
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

        // Asegurar que el rol se pasa sin "ROLE_"
        Authentication auth = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                Collections.singletonList(() -> rol.name()) // Ejemplo: "VENDEDOR"
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test //No testear segurida con poner lo del rol. solo metodos como si no tuviera seguiridad
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
                .andExpect(status().isNotFound()); // ⬅️ Cambia a 404
    }

    @Test
    void getPedidosDelComprador_WrongRole_ShouldForbid() throws Exception {
        // Configurar usuario con rol VENDEDOR
        mockAuthentication("vendedor@test.com", ERol.ROLE_VENDEDOR);

        // Mockear el servicio para devolver un usuario VENDEDOR
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(rolVendedor); // Rol incorrecto
        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.of(usuarioMock));

        // Ejecutar y verificar
        mockMvc.perform(get("/api/pedidos/mis-pedidos"))
                .andExpect(status().isForbidden());

        // Opcional: Verificar que el servicio de pedidos no se llamó
        verify(pedidoService, never()).findByComprador(any());
    }


    @Test
    void getVentasDelVendedor_UnauthorizedRole_ShouldForbid() throws Exception {
        // Configurar usuario con rol COMPRADOR
        mockAuthentication("comprador@test.com", ERol.ROLE_COMPRADOR);

        // Mockear el servicio para devolver un usuario COMPRADOR
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(rolComprador);
        when(usuarioService.findByCorreo(anyString())).thenReturn(Optional.of(usuarioMock));

        // Ejecutar y verificar
        mockMvc.perform(get("/api/pedidos/mis-ventas"))
                .andExpect(status().isForbidden());

        // Opcional: Verificar que el servicio no se llamó
        verify(pedidoService, never()).findPedidoItemsByVendedor(any());
    }



}