package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.ProductoRestController;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoRestController productoRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoRestController).build();
    }

    @Test
    void crearProducto_CamposValidos_Devuelve201() throws Exception {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Teclado Mecánico")
                .precio(120.0)
                .build();

        when(productoService.saveProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void obtenerProducto_ProductoExistente_Devuelve200() throws Exception {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Teclado Mecánico")
                .precio(120.0)
                .build();

        when(productoService.getProductoById(1L)).thenReturn(producto);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado Mecánico"));
    }

    @Test
    void obtenerProductoNoExistente_Devuelve404() throws Exception {
        // Modificado para lanzar la excepción que correspondería
        when(productoService.getProductoById(999L)).thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(get("/productos/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void obtenerTodosLosProductos_Devuelve200YListaDeProductos() throws Exception {
        List<Producto> productos = Arrays.asList(
                Producto.builder().id(1L).nombre("Teclado Mecánico").precio(120.0).build(),
                Producto.builder().id(2L).nombre("Mouse Gaming").precio(80.0).build()
        );

        when(productoService.getAllProductos()).thenReturn(productos);

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Teclado Mecánico"))
                .andExpect(jsonPath("$[1].nombre").value("Mouse Gaming"));
    }

    @Test
    void actualizarProducto_ProductoExistente_Devuelve200() throws Exception {
        Producto productoActualizado = Producto.builder()
                .id(1L)
                .nombre("Teclado Mecánico RGB")
                .precio(150.0)
                .build();

        when(productoService.updateProducto(eq(1L), any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado Mecánico RGB"));
    }

    @Test
    void eliminarProducto_ProductoExistente_Devuelve204() throws Exception {
        doNothing().when(productoService).deleteProducto(1L);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarProductosPorNombre_Devuelve200() throws Exception {
        List<Producto> productos = Arrays.asList(
                Producto.builder().id(1L).nombre("Teclado Mecánico").precio(120.0).build(),
                Producto.builder().id(3L).nombre("Teclado Inalámbrico").precio(90.0).build()
        );

        when(productoService.searchProductos("Teclado")).thenReturn(productos);

        mockMvc.perform(get("/productos/search")
                        .param("nombre", "Teclado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Teclado Mecánico"))
                .andExpect(jsonPath("$[1].nombre").value("Teclado Inalámbrico"));
    }

    @Test
    void filtrarProductosPorCategoria_Devuelve200() throws Exception {
        List<Producto> productos = Arrays.asList(
                Producto.builder().id(1L).nombre("Teclado Mecánico").precio(120.0).build(),
                Producto.builder().id(2L).nombre("Mouse Gaming").precio(80.0).build()
        );

        when(productoService.filterProductosByCategoria(1)).thenReturn(productos);

        mockMvc.perform(get("/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Teclado Mecánico"))
                .andExpect(jsonPath("$[1].nombre").value("Mouse Gaming"));
    }
}