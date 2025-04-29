package com.ecommerce.backendnpu.Api;


import com.ecommerce.backendnpu.exception.ProductoNotFoundException;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.CategoriaService;
import com.ecommerce.backendnpu.service.GoogleCloudStorageService;
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
    private final GoogleCloudStorageService storageService; // Añadir

    @Autowired
    public ProductoRestController(
            ProductoService productoService,
            CategoriaService categoriaService,
            UsuarioService usuarioService,
            GoogleCloudStorageService storageService // Inyectar
    ) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
        this.storageService = storageService; // Inicializar
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
                    // La URL ya viene poblada desde el servicio
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
            Usuario vendedor = obtenerUsuarioAutenticado();
            Categoria categoria = categoriaService.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoria(categoria);
            producto.setVendedor(vendedor);
            producto.setActivo(true);

            // Manejo de la imagen antes de guardar
            if (imagen != null && !imagen.isEmpty()) {
                String fileName = storageService.uploadFile(imagen);
                producto.setImagen(fileName);
            }

            Producto productoGuardado = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
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

        // Validar propiedad
        Usuario vendedor = obtenerUsuarioAutenticado();
        if (!producto.getVendedor().getId().equals(vendedor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Actualizar campos
        Optional.ofNullable(nombre).ifPresent(producto::setNombre);
        Optional.ofNullable(descripcion).ifPresent(producto::setDescripcion);
        Optional.ofNullable(precio).ifPresent(producto::setPrecio);
        Optional.ofNullable(stock).ifPresent(producto::setStock);

        // Actualizar categoría
        Optional.ofNullable(categoriaId).ifPresent(idCat ->
                categoriaService.findById(idCat).ifPresent(producto::setCategoria)
        );

        // Manejar imagen
        if (imagen != null && !imagen.isEmpty()) {
            if (producto.getImagen() != null) {
                storageService.deleteFile(producto.getImagen());
            }
            String nuevoNombreImagen = storageService.uploadFile(imagen);
            producto.setImagen(nuevoNombreImagen);
        }

        Producto productoActualizado = productoService.save(producto);
        return ResponseEntity.ok(productoActualizado);
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
        Producto productoActualizado = productoService.save(producto); // Sin parámetro de imagen

        return ResponseEntity.ok(productoActualizado);
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

}