package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    private Usuario vendedor;
    private Categoria categoria;
    private Producto productoActivo;
    private Producto productoInactivo;

    @BeforeEach
    void setUp() {
        // Crear y persistir usuario vendedor
        vendedor = new Usuario();
        vendedor.setNombreUsuario("Vendedor Test");
        vendedor.setCorreo("vendedor@test.com");
        vendedor.setContrasena("password");
        entityManager.persist(vendedor);

        // Crear y persistir categoría
        categoria = new Categoria();
        categoria.setNombre("Categoría Test");
        categoria.setDescripcion("Descripción de categoría test");
        entityManager.persist(categoria);

        // Crear producto activo
        productoActivo = new Producto();
        productoActivo.setNombre("Producto Activo");
        productoActivo.setDescripcion("Descripción del producto activo");
        productoActivo.setPrecio(100.0);
        productoActivo.setStock(10);
        productoActivo.setImagen("imagen_activo.jpg");
        productoActivo.setActivo(true);
        productoActivo.setCategoria(categoria);
        productoActivo.setVendedor(vendedor);
        productoActivo.setFechaCreacion(LocalDateTime.now());
        productoActivo.setFechaActualizacion(LocalDateTime.now());
        entityManager.persist(productoActivo);

        // Crear producto inactivo
        productoInactivo = new Producto();
        productoInactivo.setNombre("Producto Inactivo");
        productoInactivo.setDescripcion("Descripción del producto inactivo");
        productoInactivo.setPrecio(200.0);
        productoInactivo.setStock(20);
        productoInactivo.setImagen("imagen_inactivo.jpg");
        productoInactivo.setActivo(false);
        productoInactivo.setCategoria(categoria);
        productoInactivo.setVendedor(vendedor);
        productoInactivo.setFechaCreacion(LocalDateTime.now());
        productoInactivo.setFechaActualizacion(LocalDateTime.now());
        entityManager.persist(productoInactivo);

        entityManager.flush();
    }

    @Test
    void findByCategoria_DebeRetornarProductosDeUnaCategoria() {
        // Cuando
        List<Producto> productos = productoRepository.findByCategoria(categoria);

        // Entonces
        assertThat(productos).hasSize(2);
        assertThat(productos).contains(productoActivo, productoInactivo);
    }

    @Test
    void findByVendedor_DebeRetornarProductosDeUnVendedor() {
        // Cuando
        List<Producto> productos = productoRepository.findByVendedor(vendedor);

        // Entonces
        assertThat(productos).hasSize(2);
        assertThat(productos).contains(productoActivo, productoInactivo);
    }

    @Test
    void findByNombreContainingIgnoreCase_DebeRetornarProductosPorNombreParcial() {
        // Cuando
        List<Producto> productosActivo = productoRepository.findByNombreContainingIgnoreCase("Activo");
        List<Producto> productosInactivo = productoRepository.findByNombreContainingIgnoreCase("Inactivo");
        List<Producto> productosTodos = productoRepository.findByNombreContainingIgnoreCase("Producto");

        // Entonces
        assertThat(productosActivo).hasSize(1);
        assertThat(productosActivo).contains(productoActivo);

        assertThat(productosInactivo).hasSize(1);
        assertThat(productosInactivo).contains(productoInactivo);

        assertThat(productosTodos).hasSize(2);
        assertThat(productosTodos).contains(productoActivo, productoInactivo);
    }

    @Test
    void findByActivoTrue_DebeRetornarSoloProductosActivos() {
        // Cuando
        List<Producto> productos = productoRepository.findByActivoTrue();

        // Entonces
        assertThat(productos).hasSize(1);
        assertThat(productos).contains(productoActivo);
        assertThat(productos).doesNotContain(productoInactivo);
    }

    @Test
    void findByCategoriaAndActivoTrue_DebeRetornarProductosActivosDeUnaCategoria() {
        // Cuando
        List<Producto> productos = productoRepository.findByCategoriaAndActivoTrue(categoria);

        // Entonces
        assertThat(productos).hasSize(1);
        assertThat(productos).contains(productoActivo);
        assertThat(productos).doesNotContain(productoInactivo);
    }

    @Test
    void findByVendedorCorreo_DebeRetornarProductosDeUnVendedorPorCorreo() {
        // Cuando
        List<Producto> productos = productoRepository.findByVendedorCorreo("vendedor@test.com");

        // Entonces
        assertThat(productos).hasSize(2);
        assertThat(productos).contains(productoActivo, productoInactivo);
    }

    @Test
    void findByNombreContainingIgnoreCaseAndActivoTrue_DebeRetornarProductosActivosPorNombreParcial() {
        // Cuando
        List<Producto> productosActivo = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue("Activo");
        List<Producto> productosInactivo = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue("Inactivo");
        List<Producto> productosTodos = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue("Producto");

        // Entonces
        assertThat(productosActivo).hasSize(1);
        assertThat(productosActivo).contains(productoActivo);

        assertThat(productosInactivo).isEmpty();

        assertThat(productosTodos).hasSize(1);
        assertThat(productosTodos).contains(productoActivo);
        assertThat(productosTodos).doesNotContain(productoInactivo);
    }
}