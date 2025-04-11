package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Producto;
import java.util.List;
import java.util.Optional;


public interface ProductoService {
    List<Producto> getAllProductos();
    Optional<Producto> getProductoById(Integer id);
    Producto saveProducto(Producto producto);
    Producto updateProducto(Producto producto);
    void deleteProducto(Integer id);
    List<Producto> searchProductos(String nombre);
    List<Producto> filterProductosByCategoria(Integer categoriaId);
}
