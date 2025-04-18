package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> getAllProductos();
    Producto getProductoById(Long id);
    Producto saveProducto(Producto producto);  // Para crear nuevos productos
    Producto updateProducto(Long id, Producto producto);  // Actualizar con ID explícito
    void deleteProducto(Long id);
    List<Producto> filterProductosByCategoria(Long categoriaId);
}