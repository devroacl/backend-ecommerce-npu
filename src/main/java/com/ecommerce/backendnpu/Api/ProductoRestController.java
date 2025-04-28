package com.ecommerce.backendnpu.Api;


import com.ecommerce.backendnpu.exception.ProductoNotFoundException;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.CategoriaService;
import com.ecommerce.backendnpu.service.ProductoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    private final Path rootLocation = Paths.get("uploads/productos");

    @Autowired
    public ProductoRestController(
            ProductoService productoService,
            CategoriaService categoriaService,
            UsuarioService usuarioService
    ) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
        crearDirectorioImagenes();
    }

    private void crearDirectorioImagenes() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear directorio de imágenes", e);
        }
    }

    // ==================== ENDPOINTS PÚBLICOS ====================
    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> getProductosDisponibles() {
        return ResponseEntity.ok(productoService.findByActivoTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(producto -> {
                    if (!producto.getActivo() && !usuarioTienePermisos()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    return ResponseEntity.ok(producto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Para el método getProductosByCategoria
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getProductosByCategoria(@PathVariable Long categoriaId) {
        Optional<Categoria> categoriaOpt = categoriaService.findById(categoriaId);
        if (categoriaOpt.isPresent()) {
            List<Producto> productos = productoService.findByCategoriaAndActivoTrue(categoriaOpt.get());
            return ResponseEntity.ok(productos);
        } else {
            // En caso de error, devolver una lista vacía
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    //Agregarle mas filtros a este endpoint para que se pueda agregar buscar por ategoris
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProductos(@RequestParam String query) {
        return ResponseEntity.ok(
                productoService.findByNombreContainingAndActivoTrue(query)
        );
    }

    // ==================== ENDPOINTS PARA VENDEDORES ====================

    @PostMapping
    @PreAuthorize("hasRole('ROLE_VENDEDOR')")
    public ResponseEntity<?> crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            // Obtener el vendedor autenticado
            Usuario vendedor = obtenerUsuarioAutenticado();

            // Buscar la categoría
            Categoria categoria = categoriaService.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            // Crear y configurar el nuevo producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoria(categoria);
            producto.setVendedor(vendedor);
            producto.setActivo(true);

            // Manejar la imagen si se proporciona
            if (imagen != null && !imagen.isEmpty()) {
                producto.setImagen(guardarImagen(imagen));
            }

            // Los campos fechaCreacion y fechaActualizacion se configuran automáticamente
            // mediante métodos @PrePersist y @PreUpdate en la entidad Producto

            // Guardar el producto
            Producto productoGuardado = productoService.save(producto);

            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear el producto: " + e.getMessage());
        }
    }





    @GetMapping("/mis-productos")
    @PreAuthorize("hasRole('ROLE_VENDEDOR')")
    public ResponseEntity<?> getMisProductos() {
        Usuario vendedor = obtenerUsuarioAutenticado();
        return ResponseEntity.ok(productoService.findByVendedor(vendedor));
    }

    // Corregido - Usar ResponseEntity<?> para permitir diferentes tipos de retorno

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_VENDEDOR')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "precio", required = false) Double precio,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        Usuario vendedor = obtenerUsuarioAutenticado();
        if (!producto.getVendedor().getId().equals(vendedor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional.ofNullable(nombre).ifPresent(producto::setNombre);
        Optional.ofNullable(descripcion).ifPresent(producto::setDescripcion);
        Optional.ofNullable(precio).ifPresent(producto::setPrecio);
        Optional.ofNullable(stock).ifPresent(producto::setStock);

        Optional.ofNullable(categoriaId).ifPresent(idCat ->
                categoriaService.findById(idCat).ifPresent(producto::setCategoria)
        );

        if (imagen != null && !imagen.isEmpty()) {
            if (producto.getImagen() != null) {
                eliminarImagen(producto.getImagen());
            }
            producto.setImagen(guardarImagen(imagen));
        }

        producto.setFechaActualizacion(LocalDateTime.now());
        return ResponseEntity.ok(productoService.save(producto));
    }

    // ==================== ENDPOINTS PARA ADMINISTRADORES ====================
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cambiarEstadoProducto(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));
        producto.setActivo(activo);
        return ResponseEntity.ok(productoService.save(producto));
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioService.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    private boolean usuarioTienePermisos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private String guardarImagen(MultipartFile imagen) {
        try {
            String fileName = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Files.copy(imagen.getInputStream(), rootLocation.resolve(fileName));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage());
        }
    }

    private void eliminarImagen(String nombreArchivo) {
        try {
            Path archivo = rootLocation.resolve(nombreArchivo);
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            System.err.println("Error al eliminar imagen: " + e.getMessage());
        }
    }
}