package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void actualizarProducto_Existente_DevuelveProductoActualizado() {
        // Preparación
        Long productoId = 1L;
        Producto productoExistente = new Producto();
        productoExistente.setId(productoId);
        productoExistente.setNombre("Producto Original");
        productoExistente.setPrecio(100.0);

        Producto productoActualizado = new Producto();
        productoActualizado.setId(productoId);
        productoActualizado.setNombre("Producto Actualizado");
        productoActualizado.setPrecio(150.0);

        // Configuración del mock
        when(productoRepository.findById(anyLong())).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        // Ejecución
        Producto resultado = productoService.updateProducto(productoId, productoActualizado);

        // Verificación
        assertNotNull(resultado);
        assertEquals("Producto Actualizado", resultado.getNombre());
        assertEquals(150.0, resultado.getPrecio());
        assertEquals(productoId, resultado.getId());
    }
}