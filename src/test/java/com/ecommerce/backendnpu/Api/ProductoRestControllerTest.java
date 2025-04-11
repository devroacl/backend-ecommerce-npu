package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

@WebMvcTest(ProductoRestController.class)
public class ProductoRestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductoService productoService;

    private Producto producto1, producto2;

    // Inyección por constructor en la clase de prueba********
    ProductoRestControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper,
            ProductoService productoService // Mock del servicio
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productoService = productoService;
    }



    @Test
    void testGetProductoById_Success() throws Exception {
        // Arrange
        Mockito.when(productoService.getProductoById(1)).thenReturn(Optional.of(producto1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        // Assert
        Mockito.verify(productoService, Mockito.times(1)).getProductoById(1);
    }

    @Test
    void testGetProductoById_NotFound() throws Exception {
        // Arrange
        Mockito.when(productoService.getProductoById(100)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Assert
        Mockito.verify(productoService, Mockito.times(1)).getProductoById(100);
    }

    // ... (Otros tests para updateProducto, deleteProducto, searchProductos, filterProductos)





}
