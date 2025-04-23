package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    public void testGuardarYRecuperarProducto() {
        Producto producto = new Producto("Laptop", 1500.00);
        producto.setDescripcion("Laptop Gamer");

        Producto guardado = productoRepository.save(producto);
        Producto encontrado = productoRepository.findById(guardado.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNombre()).isEqualTo("Laptop");
        assertThat(encontrado.getPrecio()).isEqualTo(1500.00);
    }
}