package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.PedidoRestController;
import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.service.PedidoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PedidoServiceImpl pedidoService;

    @InjectMocks
    private PedidoRestController pedidoRestController;

    private Usuario usuario;
    private EstadoPedido estado;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule()); // Configura Jackson para LocalDateTime
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoRestController).build();

        // Inicialización de Rol
        Rol rol = new Rol();
        rol.setNombre(ERol.CLIENTE);

        // Configuración de Usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("Juan");
        usuario.setRol(rol); // Rol asignado

        // Configuración de EstadoPedido
        estado = new EstadoPedido();
        estado.setId(1L);
        estado.setNombreEstado("En proceso");

        // Configuración de Pedido
        pedido = new Pedido(usuario, estado, BigDecimal.valueOf(150.50));
        pedido.setId(1L);
        pedido.setFecha(LocalDateTime.now());
    }

    @Test
    void testCrearPedido() throws Exception {
        when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pedido.getId()));
    }

    @Test
    void testObtenerTodosLosPedidos() throws Exception {
        when(pedidoService.obtenerTodosLosPedidos()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testObtenerPedidoPorId() throws Exception {
        when(pedidoService.obtenerPedidoPorId(1L)).thenReturn(pedido);

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerPedidosPorUsuario() throws Exception {
        when(pedidoService.obtenerPedidosPorUsuario(1L)).thenReturn(List.of(pedido));

        mockMvc.perform(get("/pedidos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testObtenerItemsPedido() throws Exception {
        ItemsPedido item = new ItemsPedido();
        item.setId(1L);
        item.setPedido(pedido);
        item.setCantidad(2);
        item.setSubtotal(300);

        when(pedidoService.obtenerItemsPedido(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/pedidos/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testActualizarEstadoPedido() throws Exception {
        pedido.getEstadoPedido().setNombreEstado("Entregado");

        when(pedidoService.actualizarEstadoPedido(eq(1L), eq("Entregado")))
                .thenReturn(pedido);

        Map<String, String> estadoUpdate = Map.of("estado", "Entregado");

        mockMvc.perform(patch("/pedidos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoPedido.nombre").value("Entregado"));
    }

    @Test
    void testActualizarEstadoPedido_BadRequest() throws Exception {
        Map<String, String> estadoUpdate = Map.of("otraCosa", "");

        mockMvc.perform(patch("/pedidos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminarPedido() throws Exception {
        doNothing().when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActualizarPedido() throws Exception {
        Pedido pedidoActualizado = new Pedido(usuario, estado, BigDecimal.valueOf(200.00));
        pedidoActualizado.setId(1L);

        when(pedidoService.actualizarPedido(eq(1L), any(Pedido.class))).thenReturn(pedidoActualizado);

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoActualizado)))
                .andExpect(status().isOk());
    }
}