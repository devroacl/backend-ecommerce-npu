package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Api/productos")
@CrossOrigin(origins = "*") // Permite que tu frontend en React pueda hacer peticiones
public class ProductoRestController {

    private ProductoService productoService;

    public void ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    public ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET: Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    // GET: Buscar producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        return productoService.getProductoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear nuevo producto
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.saveProducto(producto));
    }

    // PUT: Actualizar producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        return productoService.getProductoById(id)
                .map(existing -> {
                    producto.setId(id); // Asegura que el ID sea el correcto
                    return ResponseEntity.ok(productoService.updateProducto(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Eliminar producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        if (productoService.getProductoById(id).isPresent()) {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET: Buscar productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> searchProductos(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.searchProductos(nombre));
    }

    // GET: Filtrar productos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> filterProductosByCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(productoService.filterProductosByCategoria(categoriaId));
    }
}
