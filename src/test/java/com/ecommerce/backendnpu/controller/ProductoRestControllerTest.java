package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.ProductoRestController;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.CategoriaService;
import com.ecommerce.backendnpu.service.ProductoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoRestControllerTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProductoRestController productoRestController;

    private Usuario vendedor;
    private Categoria categoria;
    private Producto producto;
    private MultipartFile imagenMock;

    @BeforeEach
    void setUp() throws IOException {
        // Configurar el contexto de seguridad mockeado
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("vendedor@test.com");

        // Configurar usuario vendedor
        vendedor = new Usuario();
        vendedor.setId(1L);
        vendedor.setCorreo("vendedor@test.com");
        when(usuarioService.findByCorreo("vendedor@test.com")).thenReturn(Optional.of(vendedor));

        // Configurar categoría
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
        when(categoriaService.findById(1L)).thenReturn(Optional.of(categoria));

        // Configurar producto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Smartphone");
        producto.setDescripcion("Un teléfono inteligente de última generación");
        producto.setPrecio(599.99);
        producto.setStock(10);
        producto.setCategoria(categoria);
        producto.setVendedor(vendedor);
        producto.setActivo(true);
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());

        // Configurar mock de imagen
        imagenMock = new MockMultipartFile(
                "imagen",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // Crear directorio temporal para pruebas de imágenes
        Path uploadDir = Paths.get("uploads/productos");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    @Test
    void testGetProductosDisponibles() {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(productoService.findByActivoTrue()).thenReturn(productos);

        // Ejecutar método a probar
        ResponseEntity<List<Producto>> response = productoRestController.getProductosDisponibles();

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Smartphone", response.getBody().get(0).getNombre());
    }

    @Test
    void testGetProductoById() {
        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.getProductoById(1L);

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Producto);
        assertEquals("Smartphone", ((Producto) response.getBody()).getNombre());
    }

    @Test
    void testGetProductosByCategoria() {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(categoriaService.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoService.findByCategoriaAndActivoTrue(categoria)).thenReturn(productos);

        // Ejecutar método a probar
        ResponseEntity<List<Producto>> response = productoRestController.getProductosByCategoria(1L);

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Smartphone", response.getBody().get(0).getNombre());
    }

    @Test
    void testGetProductosByCategoriaNotFound() {
        // Configurar mock para categoría no encontrada
        when(categoriaService.findById(999L)).thenReturn(Optional.empty());

        // Ejecutar método a probar
        ResponseEntity<List<Producto>> response = productoRestController.getProductosByCategoria(999L);

        // Verificar resultado
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testBuscarProductos() {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(productoService.findByNombreContainingAndActivoTrue("smart")).thenReturn(productos);

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.buscarProductos("smart");

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        assertEquals(1, ((List<Producto>) response.getBody()).size());
    }

    @Test
    void testCrearProducto() {
        // Configurar mock
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.crearProducto(
                "Smartphone",
                "Un teléfono inteligente de última generación",
                599.99,
                10,
                1L,
                imagenMock
        );

        // Verificar resultado
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testGetMisProductos() {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(productoService.findByVendedor(vendedor)).thenReturn(productos);

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.getMisProductos();

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        assertEquals(1, ((List<Producto>) response.getBody()).size());
    }

    @Test
    void testActualizarProducto() {
        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.actualizarProducto(
                1L,
                "Smartphone Pro",
                "Versión mejorada del smartphone",
                699.99,
                15,
                1L,
                null
        );

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProductoNoPermitido() {
        // Configurar producto con diferente vendedor
        Usuario otroVendedor = new Usuario();
        otroVendedor.setId(2L);

        Producto productoOtroVendedor = new Producto();
        productoOtroVendedor.setId(1L);
        productoOtroVendedor.setVendedor(otroVendedor);

        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(productoOtroVendedor));

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.actualizarProducto(
                1L,
                "Smartphone Pro",
                "Versión mejorada del smartphone",
                699.99,
                15,
                1L,
                null
        );

        // Verificar resultado
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testCambiarEstadoProducto() {
        // Crear una colección de autoridades compatible con lo que espera el método
        // Crear una colección de autoridades con el tipo correcto
        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Configurar mock para rol de administrador con el tipo correcto
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        // Ejecutar método a probar
        ResponseEntity<?> response = productoRestController.cambiarEstadoProducto(1L, false);

        // Verificar resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Producto);
        assertFalse(((Producto) response.getBody()).getActivo());
    }
}