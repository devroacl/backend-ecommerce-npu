package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.ProductoRestController;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.security.JwtUtils;
import com.ecommerce.backendnpu.security.UserDetailsServiceImpl;
import com.ecommerce.backendnpu.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductoRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductoRestControllerTest {

    @MockitoBean
    private ProductoServiceImpl productoService;

    @MockitoBean
    private UsuarioServiceImpl usuarioService;

    @MockitoBean
    private CategoriaServiceImpl categoriaService;


    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private Authentication authentication;

    @MockitoBean
    private GoogleCloudStorageService googleCloudStorageService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    private Usuario vendedor;
    private Categoria categoria;
    private Producto producto;
    private MultipartFile imagenMock;
    @Autowired
    private MockMvc mockMvc;

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
    void testGetProductosDisponibles() throws Exception {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(productoService.findByActivoTrue()).thenReturn(productos);

        mockMvc.perform(get("/api/productos/disponibles"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductoById() throws Exception {
        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Smartphone"));

    }

    @Test
    void testGetProductosByCategoria() throws Exception {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(categoriaService.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoService.findByCategoriaAndActivoTrue(categoria)).thenReturn(productos);

        mockMvc.perform(get("/api/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Smartphone"));

    }

    @Test
    void testGetProductosByCategoriaNotFound() throws Exception {
        // Configurar mock para categoría no encontrada
        when(categoriaService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/categoria/999")) // ID 999 (no existe)
                .andExpect(status().isBadRequest()) // Esperar 400, no 200
                .andExpect(jsonPath("$").isArray()) // Verificar que el cuerpo es una lista
                .andExpect(jsonPath("$").isEmpty()); // Verificar que la lista está vacía
    }

    @Test
    void testBuscarProductos() throws Exception {
        // Configurar mock
        when(productoService.findByNombreContainingAndActivoTrue("smart")).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/buscar")
                        .param("query", "smart")) // Parámetro de búsqueda
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Smartphone"));
    }
    
    @Test
    void testCrearProducto() throws Exception {
        // 1. Mockear el método que obtiene el usuario autenticado
        Usuario vendedorMock = new Usuario();
        vendedorMock.setId(1L);
        vendedorMock.setCorreo("vendedor@test.com");

        // Mockear el servicio para devolver el vendedor
        when(usuarioService.findByCorreo("vendedor@test.com")).thenReturn(Optional.of(vendedorMock));

        // 2. Mockear el servicio de almacenamiento de imágenes
        when(googleCloudStorageService.uploadFile(any())).thenReturn("imagen_test.jpg");

        // 3. Configurar el producto esperado
        Producto productoEsperado = new Producto();
        productoEsperado.setNombre("Laptop");
        productoEsperado.setDescripcion("Descripción");
        productoEsperado.setPrecio(999.99);
        productoEsperado.setStock(5);
        productoEsperado.setCategoria(categoria);
        productoEsperado.setVendedor(vendedorMock);
        productoEsperado.setImagen("imagen_test.jpg");

        when(productoService.save(any())).thenReturn(productoEsperado);

        // 4. Simular la solicitud POST con imagen
        MockMultipartFile imagenMock = new MockMultipartFile(
                "imagen",
                "test.jpg",
                "image/jpeg",
                "contenido_imagen".getBytes()
        );

        mockMvc.perform(multipart("/api/productos")
                        .file(imagenMock)
                        .param("nombre", "Laptop")
                        .param("descripcion", "Descripción")
                        .param("precio", "999.99")
                        .param("stock", "5")
                        .param("categoriaId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.imagen").value("imagen_test.jpg"));
    }

    @Test
    void testGetMisProductos()throws Exception {
        // Preparar datos de prueba
        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        // Configurar mock
        when(productoService.findByVendedor(vendedor)).thenReturn(productos);

        mockMvc.perform(get("/api/productos/mis-productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    void testActualizarProducto() throws Exception {
        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        when(productoService.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            return p; // Devuelve el producto actualizado
        });

        // Simular solicitud PUT
        mockMvc.perform(put("/api/productos/1")
                        .param("nombre", "Smartphone Pro")
                        .param("descripcion", "Nueva descripción"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Smartphone Pro")); // Verificar el nuevo nombre
    }

    @Test
    void testActualizarProductoNoPermitido() throws Exception {
        // Configurar producto con diferente vendedor
        Usuario otroVendedor = new Usuario();
        otroVendedor.setId(2L);

        Producto productoOtroVendedor = new Producto();
        productoOtroVendedor.setId(1L);
        productoOtroVendedor.setVendedor(otroVendedor);

        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(productoOtroVendedor));

        mockMvc.perform(put("/api/productos/1")
                        .param("nombre", "Nombre Nuevo"))
                .andExpect(status().isForbidden());
    }


    @Test
    void testCambiarEstadoProducto()throws Exception  {
        // Crear una colección de autoridades compatible con lo que espera el método
        // Crear una colección de autoridades con el tipo correcto
        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Configurar mock para rol de administrador con el tipo correcto
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        // Configurar mock
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(patch("/api/productos/1/estado")
                        .param("activo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));


    }
}