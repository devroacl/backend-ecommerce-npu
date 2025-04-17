package com.ecommerce.backendnpu.repository;
// ProductoRepositoryTest.java


import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ProductoRepositoryTest {

    private ProductoRepository productoRepository; // Asume que existe "ProductoRepository"

    @Test
    public void testGuardarProducto() {
        Producto producto = new Producto("Laptop", 1500.00);
        Producto guardado = productoRepository.save(producto);
        assertThat(guardado.getId()).isNotNull();
    }
}