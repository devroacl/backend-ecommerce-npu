package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.PedidoRestController;
import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.service.PedidoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoRestController.class)
class PedidoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PedidoServiceImpl pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private EstadoPedido estado;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("Juan");

        estado = new EstadoPedido();
        estado.setId(1L);
        estado.setNombreEstado("En proceso");

        pedido = new Pedido(usuario, estado, BigDecimal.valueOf(150.50));
        pedido.setId(1L);
        pedido.setFecha(LocalDateTime.now());
    }

    @Test
    void testCrearPedido() throws Exception {
        Mockito.when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pedido.getId()));
    }

    @Test
    void testObtenerTodosLosPedidos() throws Exception {
        List<Pedido> pedidos = List.of(pedido);
        Mockito.when(pedidoService.obtenerTodosLosPedidos()).thenReturn(pedidos);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testObtenerPedidoPorId() throws Exception {
        Mockito.when(pedidoService.obtenerPedidoPorId(1L)).thenReturn(pedido);

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerPedidosPorUsuario() throws Exception {
        List<Pedido> pedidos = List.of(pedido);
        Mockito.when(pedidoService.obtenerPedidosPorUsuario(1L)).thenReturn(pedidos);

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
        item.setProducto(null); // Simulación mínima

        Mockito.when(pedidoService.obtenerItemsPedido(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/pedidos/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testActualizarEstadoPedido() throws Exception {
        pedido.getEstadoPedido().setNombreEstado("Entregado");

        Mockito.when(pedidoService.actualizarEstadoPedido(eq(1L), eq("Entregado")))
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
        Map<String, String> estadoUpdate = Map.of("otraCosa", ""); // clave incorrecta

        mockMvc.perform(patch("/pedidos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminarPedido() throws Exception {
        Mockito.doNothing().when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActualizarPedido() throws Exception {
        Mockito.doNothing().when(pedidoService).actualizarPedido(1L);

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());
    }
}
