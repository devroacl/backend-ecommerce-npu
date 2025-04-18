package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductoRepositorytest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    private Categoria categoria1;
    private Categoria categoria2;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;

    @BeforeEach
    void setUp() {
        // Crear categorías de prueba
        categoria1 = new Categoria();
        categoria1.setNombre("Electrónicos");
        entityManager.persist(categoria1);

        categoria2 = new Categoria();
        categoria2.setNombre("Muebles");
        entityManager.persist(categoria2);

        // Crear productos de prueba
        producto1 = Producto.builder()
                .nombre("Laptop")
                .descripcion("Laptop de alta gama")
                .precio(1500.0)
                .cantidad(10)
                .categoria(categoria1)
                .build();

        producto2 = Producto.builder()
                .nombre("Smartphone")
                .descripcion("Teléfono inteligente")
                .precio(800.0)
                .cantidad(20)
                .categoria(categoria1)
                .build();

        producto3 = Producto.builder()
                .nombre("Silla")
                .descripcion("Silla ergonómica")
                .precio(200.0)
                .cantidad(15)
                .categoria(categoria2)
                .build();

        // Persistir los productos
        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.persist(producto3);

        // Flush para asegurar que las entidades se guarden en la base de datos
        entityManager.flush();
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        // Act
        List<Producto> productos = productoRepository.findAll();

        // Assert
        assertThat(productos).isNotNull();
        assertThat(productos).hasSize(3);
        assertThat(productos).extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Laptop", "Smartphone", "Silla");
    }

    @Test
    void findById_WhenValidId_ShouldReturnProduct() {
        // Importante: Obtenemos el ID actual asignado a producto1
        Long productoId = producto1.getId();

        // Act
        Optional<Producto> found = productoRepository.findById(productoId);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Laptop");
        assertThat(found.get().getPrecio()).isEqualTo(1500.0);
    }

    @Test
    void findById_WhenInvalidId_ShouldReturnEmpty() {
        // Act
        Optional<Producto> found = productoRepository.findById(999L);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void save_ShouldPersistProduct() {
        // Arrange
        Producto nuevoProducto = Producto.builder()
                .nombre("Monitor")
                .descripcion("Monitor 4K")
                .precio(300.0)
                .cantidad(5)
                .categoria(categoria1)
                .build();

        // Act
        Producto saved = productoRepository.save(nuevoProducto);

        // Assert
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();

        Producto found = entityManager.find(Producto.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getNombre()).isEqualTo("Monitor");
        assertThat(found.getPrecio()).isEqualTo(300.0);
    }

    @Test
    void update_ShouldUpdateProduct() {
        // Arrange - Obtenemos el ID actual antes de modificar
        Long productoId = producto1.getId();
        Producto productoToUpdate = entityManager.find(Producto.class, productoId);
        productoToUpdate.setNombre("Laptop Actualizado");
        productoToUpdate.setPrecio(1800.0);

        // Act
        Producto updated = productoRepository.save(productoToUpdate);

        // Assert
        Producto found = entityManager.find(Producto.class, productoId);
        assertThat(found).isNotNull();
        assertThat(found.getNombre()).isEqualTo("Laptop Actualizado");
        assertThat(found.getPrecio()).isEqualTo(1800.0);
    }

    @Test
    void deleteById_ShouldRemoveProduct() {
        // Arrange - Obtenemos el ID actual antes de eliminar
        Long productoId = producto1.getId();

        // Act
        productoRepository.deleteById(productoId);

        // Assert
        Producto found = entityManager.find(Producto.class, productoId);
        assertThat(found).isNull();
    }

    @Test
    void findByCategoriaId_WhenValidCategoriaId_ShouldReturnProducts() {
        // Arrange - Usamos el ID real de la categoría
        Long categoriaId = categoria1.getId();

        // Act
        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);

        // Assert
        assertThat(productos).isNotNull();
        assertThat(productos).hasSize(2);
        assertThat(productos).extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Laptop", "Smartphone");
    }

    @Test
    void findByCategoriaId_WhenInvalidCategoriaId_ShouldReturnEmptyList() {
        // Act
        List<Producto> productos = productoRepository.findByCategoriaId(999L);

        // Assert
        assertThat(productos).isNotNull();
        assertThat(productos).isEmpty();
    }

    @Test
    void findByCategoriaId_WhenCategoriaHasNoProducts_ShouldReturnEmptyList() {
        // Arrange
        Categoria categoria3 = new Categoria();
        categoria3.setNombre("Libros");
        entityManager.persist(categoria3);

        // Act
        List<Producto> productos = productoRepository.findByCategoriaId(categoria3.getId());

        // Assert
        assertThat(productos).isNotNull();
        assertThat(productos).isEmpty();
    }
}