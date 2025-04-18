package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.ProductoRestController;
import com.ecommerce.backendnpu.exception.GlobalExceptionHandler;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ProductoRestController controller = new ProductoRestController(productoService);

        // Configurar MockMvc con el manejador global de excepciones
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    // Helper para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    // Tests para POST /productos
    @Test
    void crearProducto_WhenValidRequest_Returns201Created() throws Exception {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Laptop")
                .descripcion("Laptop de última generación")
                .precio(1500.0)
                .cantidad(10)
                .categoria(categoria)
                .build();

        when(productoService.saveProducto(any(Producto.class))).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.precio").value(1500.0));

        verify(productoService, times(1)).saveProducto(any(Producto.class));
    }

    @Test
    void crearProducto_WhenServiceThrowsException_Returns404() throws Exception {
        // Arrange
        Producto producto = new Producto();
        when(productoService.saveProducto(any(Producto.class))).thenThrow(new RuntimeException("Error al guardar"));

        // Act & Assert
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(producto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Error al guardar"));
    }

    // Tests para GET /productos
    @Test
    void obtenerTodosLosProductos_WhenProductsExist_ReturnsList() throws Exception {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        List<Producto> productos = Arrays.asList(
                Producto.builder().id(1L).nombre("Laptop").descripcion("Potente").precio(1500.0).cantidad(5).categoria(categoria).build(),
                Producto.builder().id(2L).nombre("Mouse").descripcion("Ergonómico").precio(20.0).cantidad(20).categoria(categoria).build()
        );

        when(productoService.getAllProductos()).thenReturn(productos);

        // Act & Assert
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(productoService, times(1)).getAllProductos();
    }

    @Test
    void obtenerTodosLosProductos_WhenEmpty_ReturnsEmptyList() throws Exception {
        // Arrange
        when(productoService.getAllProductos()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productoService, times(1)).getAllProductos();
    }

    // Tests para GET /productos/{id}
    @Test
    void obtenerProductoPorId_WhenExists_ReturnsProduct() throws Exception {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Teclado")
                .descripcion("Mecánico")
                .precio(50.0)
                .cantidad(15)
                .categoria(categoria)
                .build();

        when(productoService.getProductoById(1L)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Teclado"))
                .andExpect(jsonPath("$.precio").value(50.0));

        verify(productoService, times(1)).getProductoById(1L);
    }

    @Test
    void obtenerProductoPorId_WhenNotExists_Returns404() throws Exception {
        // Arrange
        when(productoService.getProductoById(99L)).thenThrow(new RuntimeException("Producto no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));

        verify(productoService, times(1)).getProductoById(99L);
    }

    // Tests para DELETE /productos/{id}
    @Test
    void eliminarProducto_WhenExists_Returns204() throws Exception {
        // Arrange
        doNothing().when(productoService).deleteProducto(1L);

        // Act & Assert
        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).deleteProducto(1L);
    }

    @Test
    void eliminarProducto_WhenNotExists_Returns404() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Producto no encontrado")).when(productoService).deleteProducto(99L);

        // Act & Assert
        mockMvc.perform(delete("/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));

        verify(productoService, times(1)).deleteProducto(99L);
    }

    // Tests para PUT /productos/{id}
    @Test
    void actualizarProducto_WhenValidRequest_ReturnsUpdatedProduct() throws Exception {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Producto productoActualizado = Producto.builder()
                .id(1L)
                .nombre("Laptop Pro")
                .descripcion("Ultra potente")
                .precio(2000.0)
                .cantidad(8)
                .categoria(categoria)
                .build();

        when(productoService.updateProducto(eq(1L), any(Producto.class))).thenReturn(productoActualizado);

        // Act & Assert
        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Laptop Pro"))
                .andExpect(jsonPath("$.precio").value(2000.0));

        verify(productoService, times(1)).updateProducto(eq(1L), any(Producto.class));
    }

    @Test
    void actualizarProducto_WhenNotExists_Returns404() throws Exception {
        // Arrange
        Producto producto = new Producto();
        when(productoService.updateProducto(eq(99L), any(Producto.class))).thenThrow(new RuntimeException("Producto no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(producto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));

        verify(productoService, times(1)).updateProducto(eq(99L), any(Producto.class));
    }

    // Tests para GET /productos/categoria/{categoriaId}
    @Test
    void obtenerPorCategoria_WhenCategoriaExists_ReturnsFilteredList() throws Exception {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        List<Producto> productosFiltrados = Arrays.asList(
                Producto.builder().id(1L).nombre("Laptop").descripcion("Gaming").precio(1500.0).cantidad(5).categoria(categoria).build(),
                Producto.builder().id(3L).nombre("PC Desktop").descripcion("Workstation").precio(2000.0).cantidad(3).categoria(categoria).build()
        );

        when(productoService.filterProductosByCategoria(1L)).thenReturn(productosFiltrados);

        // Act & Assert
        mockMvc.perform(get("/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[1].nombre").value("PC Desktop"));

        verify(productoService, times(1)).filterProductosByCategoria(1L);
    }

    @Test
    void obtenerPorCategoria_WhenCategoriaHasNoProducts_ReturnsEmptyList() throws Exception {
        // Arrange
        when(productoService.filterProductosByCategoria(2L)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/productos/categoria/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productoService, times(1)).filterProductosByCategoria(2L);
    }

    @Test
    void obtenerPorCategoria_WhenServiceThrowsException_Returns404() throws Exception {
        // Arrange
        when(productoService.filterProductosByCategoria(99L)).thenThrow(new RuntimeException("Categoría no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/productos/categoria/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoría no encontrada"));

        verify(productoService, times(1)).filterProductosByCategoria(99L);
    }
}