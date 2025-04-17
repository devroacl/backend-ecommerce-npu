package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoRepositoryTest {

    @Mock
    private ProductoRepository productoRepository;

    @Test
    void findAll_ShouldReturnAllProducts() {
        // Arrange
        List<Producto> sampleProducts = TestData.getSampleProducts();
        when(productoRepository.findAll()).thenReturn(sampleProducts);

        // Act
        List<Producto> result = productoRepository.findAll();

        // Assert
        assertEquals(5, result.size(), "Deberían haber 5 productos");
        assertEquals("Laptop", result.get(0).getNombre(), "El primer producto debería ser Laptop");
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void findById_ExistingId_ShouldReturnProduct() {
        // Arrange
        Producto sample = TestData.getSampleProducts().get(0);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(sample));

        // Act
        Optional<Producto> result = productoRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent(), "Debería encontrar el producto");
        assertEquals(999.99, result.get().getPrecio(), 0.001, "El precio debería coincidir");
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Producto> result = productoRepository.findById(999L);

        // Assert
        assertTrue(result.isEmpty(), "No debería encontrar el producto");
        verify(productoRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldPersistProduct() {
        // Arrange
        Producto newProduct = new Producto(6L, "Webcam", 45.00);
        when(productoRepository.save(newProduct)).thenReturn(newProduct);

        // Act
        Producto savedProduct = productoRepository.save(newProduct);

        // Assert
        assertNotNull(savedProduct, "El producto guardado no debería ser null");
        assertEquals("Webcam", savedProduct.getNombre());
        verify(productoRepository, times(1)).save(newProduct);
    }

    @Test
    void delete_ShouldRemoveProduct() {
        // Arrange
        doNothing().when(productoRepository).deleteById(1L);

        // Act
        productoRepository.deleteById(1L);

        // Assert
        verify(productoRepository, times(1)).deleteById(1L);
    }
}