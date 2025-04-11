package com.ecommerce.backendnpu.Service;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import com.ecommerce.backendnpu.service.ProductoService;
import com.ecommerce.backendnpu.service.ProductoServiceImpl;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    private Producto producto1, producto2;

    // Inyección por constructor (usando Mockito)
    ProductoServiceImplTest(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        this.productoService = new ProductoServiceImpl(productoRepository); // Instancia el servicio con el mock
    }

    @BeforeEach
    void setUp() {
        producto1 = new Producto();
        producto1.setId(1);
        producto1.setNombre("Test Producto 1");
        producto1.setPrecio(10.0);

        producto2 = new Producto();
        producto2.setId(2);
        producto2.setNombre("Test Producto 2");
        producto2.setPrecio(20.0);
    }


    @Test
    void testSaveProducto_Success() {
        // Arrange
        Producto productoToSave = new Producto();
        productoToSave.setNombre("New Producto");
        when(productoRepository.save(any(Producto.class))).thenReturn(producto1);

        // Act
        Producto savedProducto = productoService.saveProducto(productoToSave);

        // Assert
        assertEquals(producto1, savedProducto);
        verify(productoRepository, times(1)).save(productoToSave);
    }

    @Test
    void testGetAllProductos_EmptyList() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Producto> productos = productoService.getAllProductos();

        // Assert
        assertTrue(productos.isEmpty());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGetProductoById_Success() {
        // Arrange
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));

        // Act
        Optional<Producto> producto = productoService.getProductoById(1);

        // Assert
        assertTrue(producto.isPresent());
        assertEquals("Test Producto 1", producto.get().getNombre());
        verify(productoRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductoById_NotFound() {
        // Arrange
        when(productoRepository.findById(100)).thenReturn(Optional.empty());

        // Act
        Optional<Producto> producto = productoService.getProductoById(100);

        // Assert
        assertTrue(producto.isEmpty());
        verify(productoRepository, times(1)).findById(100);
    }


    @Test
    void testUpdateProducto_Success() {
        // Arrange
        Producto productoToUpdate = new Producto();
        productoToUpdate.setId(1);
        productoToUpdate.setNombre("Updated Producto");
        when(productoRepository.save(any(Producto.class))).thenReturn(productoToUpdate);

        // Act
        Producto updatedProducto = productoService.updateProducto(productoToUpdate);

        // Assert
        assertEquals(productoToUpdate, updatedProducto);
        verify(productoRepository, times(1)).save(productoToUpdate);
    }

    @Test
    void testDeleteProducto_Success() {
        // Arrange
        doNothing().when(productoRepository).deleteById(1);

        // Act
        productoService.deleteProducto(1);

        // Assert
        verify(productoRepository, times(1)).deleteById(1);
    }


    @Test
    void testSearchProductos_NoResults() {
        // Arrange
        when(productoRepository.findByNombreContaining("NonExistent")).thenReturn(Collections.emptyList());

        // Act
        List<Producto> foundProducts = productoService.searchProductos("NonExistent");

        // Assert
        assertTrue(foundProducts.isEmpty());
        verify(productoRepository, times(1)).findByNombreContaining("NonExistent");
    }

   // @Test crear test para ver si el filtro por categoria sirve
    //void testFilterProductosByCategoria_Success() {


    @Test
    void testFilterProductosByCategoria_NoResults() {
        // Arrange
        when(productoRepository.findByCategoriaId(100)).thenReturn(Collections.emptyList());

        // Act
        List<Producto> foundProducts = productoService.filterProductosByCategoria(100);

        // Assert
        assertTrue(foundProducts.isEmpty());
        verify(productoRepository, times(1)).findByCategoriaId(100);
    }

    // ... (Otros tests)




}
