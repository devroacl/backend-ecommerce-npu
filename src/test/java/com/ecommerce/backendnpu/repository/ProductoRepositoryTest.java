// ProductoRepositoryTest.java
package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    private void insertTestData() {
        Producto p1 = new Producto(1L, "Laptop", 1500.00, 1L);
        Producto p2 = new Producto(2L, "Mouse", 25.99, 1L);
        Producto p3 = new Producto(3L, "Teclado", 45.50, 1L);
        Producto p4 = new Producto(4L, "Monitor", 300.00, 1L);
        Producto p5 = new Producto(5L, "Cargador", 30.00, 2L);
        productoRepository.saveAll(List.of(p1, p2, p3, p4, p5));
    }

    @Test
    void findByNombreContaining_ShouldReturnProducts() {
        insertTestData();
        List<Producto> resultados = productoRepository.findByNombreContaining("Mo");
        assertEquals(2, resultados.size()); // Mouse y Monitor
    }

    @Test
    void findByCategoriaId_ShouldReturnProducts() {
        insertTestData();
        List<Producto> resultados = productoRepository.findByCategoriaId(1L);
        assertEquals(4, resultados.size()); // 4 productos en categoría 1
    }
}