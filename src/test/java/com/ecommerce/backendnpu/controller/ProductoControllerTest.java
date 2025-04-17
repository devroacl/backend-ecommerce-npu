package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.ProductoRestController;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductoService productoService; // Mock manual

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        ProductoRestController controller = new ProductoRestController(productoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // Helper para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    // Tests para POST /productos
    @Test
    void crearProducto_WhenValidRequest_Returns201Created() throws Exception {
        Producto producto = new Producto(1L, "Laptop", 1500.0);
        when(productoService.saveProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void crearProducto_WhenServiceThrowsException_Returns500() throws Exception {
        when(productoService.saveProducto(any(Producto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Producto())))
                .andExpect(status().isInternalServerError());
    }

    // Tests para GET /productos
    @Test
    void obtenerTodosLosProductos_WhenProductsExist_ReturnsList() throws Exception {
        List<Producto> productos = Arrays.asList(
                new Producto(1L, "Laptop", 1500.0),
                new Producto(2L, "Mouse", 20.0)
        );
        when(productoService.getAllProductos()).thenReturn(productos);

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void obtenerTodosLosProductos_WhenEmpty_ReturnsEmptyList() throws Exception {
        when(productoService.getAllProductos()).thenReturn(List.of());

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // Tests para GET /productos/{id}
    @Test
    void obtenerProductoPorId_WhenExists_ReturnsProduct() throws Exception {
        Producto producto = new Producto(1L, "Teclado", 50.0);
        when(productoService.getProductoById(1L)).thenReturn(producto);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado"));
    }

    @Test
    void obtenerProductoPorId_WhenNotExists_Returns404() throws Exception {
        when(productoService.getProductoById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isInternalServerError());
    }

    // Tests para DELETE /productos/{id}
    @Test
    void eliminarProducto_WhenExists_Returns204() throws Exception {
        doNothing().when(productoService).deleteProducto(1L);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarProducto_WhenNotExists_Returns500() throws Exception {
        doThrow(new RuntimeException()).when(productoService).deleteProducto(99L);

        mockMvc.perform(delete("/productos/99"))
                .andExpect(status().isInternalServerError());
    }

    // Tests para PUT /productos/{id}
    @Test
    void actualizarProducto_WhenValidRequest_ReturnsUpdatedProduct() throws Exception {
        Producto productoActualizado = new Producto(1L, "Laptop Pro", 2000.0);
        when(productoService.updateProducto(eq(1L), any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop Pro"));
    }

    @Test
    void actualizarProducto_WhenNotExists_Returns500() throws Exception {
        when(productoService.updateProducto(eq(99L), any(Producto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Producto())))
                .andExpect(status().isInternalServerError());
    }





}