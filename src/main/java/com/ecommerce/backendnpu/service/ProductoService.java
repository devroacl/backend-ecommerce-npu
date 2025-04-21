package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Producto;
import java.util.List;

public interface ProductoService{

    // Métodos para obtener productos
    List<Producto> getAllProductos();  // Para admin - todos los productos
    List<Producto> obtenerTodosLosProductosActivos();  // Para público - solo productos activos
    Producto getProductoById(Long id);  // Obtener cualquier producto por ID
    Producto obtenerProductoActivoPorId(Long id);  // Obtener solo productos activos por ID

    // Métodos para filtrar productos
    List<Producto> filterProductosByCategoria(Long categoriaId);  // Filtrar por categoría
    List<Producto> obtenerProductosPorVendedor(String correoVendedor);  // Productos por vendedor

    // Métodos para gestionar productos (vendedor)
    Producto saveProducto(Producto producto, String correoVendedor);
    Producto actualizarProducto(Long id, Producto producto, String correoVendedor);
    void eliminarProducto(Long id, String correoVendedor);

    // Métodos para administración (admin)
    Producto bloquearProducto(Long id);
    Producto desbloquearProducto(Long id);


}