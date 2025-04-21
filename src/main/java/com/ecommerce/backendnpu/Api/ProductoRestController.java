package com.ecommerce.backendnpu.Api;


import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoService productoService;

    // Endpoints públicos
    @GetMapping("/public/todos")
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductosActivos();
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoActivoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/public/categoria/{categoriaId}")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable Long categoriaId) {
        return productoService.filterProductosByCategoria(categoriaId);
    }

    // Endpoints para vendedores
    @PostMapping("/crear")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();

        Producto nuevoProducto = productoService.saveProducto(producto,correo);
        return ResponseEntity.ok(nuevoProducto);
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<?> editarProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();

            Producto producto = productoService.actualizarProducto(id, productoActualizado, correo);
            return ResponseEntity.ok(producto);
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String correo = authentication.getName();

            productoService.eliminarProducto(id, correo);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoints para administradores
    @PutMapping("/admin/bloquear/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> bloquearProducto(@PathVariable Long id) {
        Producto producto = productoService.bloquearProducto(id);
        return ResponseEntity.ok("Producto bloqueado correctamente");
    }

    @PutMapping("/admin/desbloquear/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desbloquearProducto(@PathVariable Long id) {
        Producto producto = productoService.desbloquearProducto(id);
        return ResponseEntity.ok("Producto desbloqueado correctamente");
    }

    // Endpoint para que los vendedores vean sus propios productos
    @GetMapping("/mis-productos")
    @PreAuthorize("hasRole('VENDEDOR')")
    public List<Producto> obtenerProductosDelVendedor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();

        return productoService.obtenerProductosPorVendedor(correo);
    }

    // Endpoint para administradores - ver todos los productos (incluso bloqueados)
    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> obtenerAbsolutamenteTodosLosProductos() {
        return productoService.getAllProductos();
    }
}