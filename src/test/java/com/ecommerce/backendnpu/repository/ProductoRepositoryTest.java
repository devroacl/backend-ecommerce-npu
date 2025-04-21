package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductoRepositoryTest {


    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private Categoria categoria;
    private Usuario vendedor;
    private Rol rol;

    @BeforeEach
    void setUp() {
        // Crear rol para usuario vendedor
        rol = new Rol();
        rol.setNombre(ERol.ROLE_VENDEDOR);
        rol = rolRepository.save(rol);

        // Crear y guardar categoría
        categoria = new Categoria();
        categoria.setNombre("Electrónica");
        categoria = categoriaRepository.save(categoria);

        // Crear y guardar usuario vendedor
        vendedor = new Usuario();
        vendedor.setNombreUsuario("Vendedor");
        vendedor.setApellido("Test");
        vendedor.setCorreo("vendedor@test.com");
        vendedor.setRut("12345678-9");
        vendedor.setVerificar(true);
        vendedor.setContrasena("password123");
        vendedor.setRol(rol);
        vendedor = usuarioRepository.save(vendedor);

        // Limpiar productos de tests anteriores
        productoRepository.deleteAll();
    }

    @Test
    @DisplayName("Test guardar producto exitosamente")
    void testGuardarProducto() {
        // Given
        Producto producto = crearProducto("Auriculares", 59.99);

        // When
        Producto guardado = productoRepository.save(producto);

        // Then
        assertThat(guardado).isNotNull();
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getNombre()).isEqualTo("Auriculares");
        assertThat(guardado.getPrecio()).isEqualTo(59.99);
        assertThat(guardado.getStock()).isEqualTo(10);
        assertThat(guardado.getCategoria().getId()).isEqualTo(categoria.getId());
        assertThat(guardado.getVendedor().getId()).isEqualTo(vendedor.getId());
    }

    @Test
    @DisplayName("Test buscar producto por ID existente")
    void testBuscarPorIdExistente() {
        // Given
        Producto producto = productoRepository.save(crearProducto("Teclado", 45.0));

        // When
        Optional<Producto> encontrado = productoRepository.findById(producto.getId());

        // Then
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Teclado");
        assertThat(encontrado.get().getPrecio()).isEqualTo(45.0);
    }

    @Test
    @DisplayName("Test buscar producto por ID inexistente")
    void testBuscarPorIdInexistente() {
        // When
        Optional<Producto> encontrado = productoRepository.findById(999L);

        // Then
        assertThat(encontrado).isNotPresent();
    }

    @Test
    @DisplayName("Test obtener todos los productos")
    void testFindAll() {
        // Given
        productoRepository.save(crearProducto("Monitor", 200.0));
        productoRepository.save(crearProducto("Mouse", 25.0));

        // When
        List<Producto> productos = productoRepository.findAll();

        // Then
        assertThat(productos).hasSize(2);
        assertThat(productos).extracting(Producto::getNombre).containsExactlyInAnyOrder("Monitor", "Mouse");
    }

    @Test
    @DisplayName("Test cuando no hay productos")
    void testFindAllSinProductos() {
        // Given - no hay productos guardados

        // When
        List<Producto> productos = productoRepository.findAll();

        // Then
        assertThat(productos).isEmpty();
    }

    @Test
    @DisplayName("Test eliminar producto")
    void testEliminarProducto() {
        // Given
        Producto producto = productoRepository.save(crearProducto("Parlante", 75.0));
        Long id = producto.getId();

        // When
        productoRepository.delete(producto);
        Optional<Producto> eliminado = productoRepository.findById(id);

        // Then
        assertThat(eliminado).isNotPresent();
    }

    @Test
    @DisplayName("Test actualizar producto")
    void testActualizarProducto() {
        // Given
        Producto producto = productoRepository.save(crearProducto("Laptop", 900.0));

        // When
        producto.setNombre("Laptop Premium");
        producto.setPrecio(1200.0);
        producto.setStock(5);
        Producto actualizado = productoRepository.save(producto);

        // Then
        assertThat(actualizado.getNombre()).isEqualTo("Laptop Premium");
        assertThat(actualizado.getPrecio()).isEqualTo(1200.0);
        assertThat(actualizado.getStock()).isEqualTo(5);
        assertThat(actualizado.getId()).isEqualTo(producto.getId());
    }

    @Test
    @DisplayName("Test buscar productos por categoría existente")
    void testFindByCategoriaIdExistente() {
        // Given
        productoRepository.save(crearProducto("TV", 800.0));
        productoRepository.save(crearProducto("Tablet", 250.0));

        // When
        List<Producto> resultado = productoRepository.findByCategoriaId(categoria.getId());

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Producto::getNombre).containsExactlyInAnyOrder("TV", "Tablet");
    }

    @Test
    @DisplayName("Test buscar productos por categoría sin productos")
    void testFindByCategoriaIdSinProductos() {
        // Given
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Ropa");
        nuevaCategoria = categoriaRepository.save(nuevaCategoria);

        // When
        List<Producto> resultado = productoRepository.findByCategoriaId(nuevaCategoria.getId());

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Test buscar productos por categoría con múltiples categorías")
    void testFindByCategoriaIdMultiplesCategorias() {
        // Given
        productoRepository.save(crearProducto("TV", 800.0));

        Categoria otraCategoria = new Categoria();
        otraCategoria.setNombre("Hogar");
        otraCategoria = categoriaRepository.save(otraCategoria);

        Producto productoOtraCategoria = crearProducto("Licuadora", 150.0);
        productoOtraCategoria.setCategoria(otraCategoria);
        productoRepository.save(productoOtraCategoria);

        // When
        List<Producto> resultadoCategoriaElectronica = productoRepository.findByCategoriaId(categoria.getId());
        List<Producto> resultadoCategoriaHogar = productoRepository.findByCategoriaId(otraCategoria.getId());

        // Then
        assertThat(resultadoCategoriaElectronica).hasSize(1);
        assertThat(resultadoCategoriaElectronica).extracting(Producto::getNombre).containsExactlyInAnyOrder("TV");

        assertThat(resultadoCategoriaHogar).hasSize(1);
        assertThat(resultadoCategoriaHogar).extracting(Producto::getNombre).containsExactlyInAnyOrder("Licuadora");
    }

    private Producto crearProducto(String nombre, Double precio) {
        return Producto.builder()
                .nombre(nombre)
                .descripcion(nombre + " de calidad")
                .precio(precio)
                .stock(10) // Valor por defecto para stock
                .categoria(categoria)
                .vendedor(vendedor)
                .build();
    }
    }
