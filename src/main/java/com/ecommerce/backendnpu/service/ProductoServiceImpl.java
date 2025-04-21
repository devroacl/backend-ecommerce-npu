package com.ecommerce.backendnpu.service;


import com.ecommerce.backendnpu.exception.ProductoNotFoundException;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import com.ecommerce.backendnpu.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> obtenerTodosLosProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Producto obtenerProductoActivoPorId(Long id) {
        return productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto activo no encontrado con ID: " + id));
    }

    @Override
    public List<Producto> filterProductosByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    @Override
    public List<Producto> obtenerProductosPorVendedor(String correoVendedor) {
        return productoRepository.findByVendedorCorreo(correoVendedor);
    }

    @Override
    public Producto saveProducto(Producto producto, String correoVendedor) {
        // Validaciones básicas
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        // Obtener el usuario vendedor
        Usuario vendedor = usuarioRepository.findByCorreo(correoVendedor)
                .orElseThrow(() -> new IllegalArgumentException("Vendedor no encontrado"));

        // Establecer el vendedor del producto
        producto.setVendedor(vendedor);

        // Por defecto, el producto está activo al crearse
        producto.setActivo(true);

        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto productoActualizado, String correoVendedor) {
        Producto productoExistente = getProductoById(id);

        // Verificar que el producto pertenece al vendedor que intenta actualizarlo
        if (!productoExistente.getVendedor().getCorreo().equals(correoVendedor)) {
            throw new SecurityException("No tienes permiso para editar este producto");
        }

        // Actualizar los campos
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());

        // Si se actualiza la categoría
        if (productoActualizado.getCategoria() != null) {
            productoExistente.setCategoria(productoActualizado.getCategoria());
        }

        // Actualizar otros campos relevantes
        if (productoActualizado.getImagen() != null) {
            productoExistente.setImagen(productoActualizado.getImagen());
        }

        if (productoActualizado.getStock() != null) {
            productoExistente.setStock(productoActualizado.getStock());
        }

        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id, String correoVendedor) {
        Producto producto = getProductoById(id);

        // Verificar que el producto pertenece al vendedor que intenta eliminarlo
        if (!producto.getVendedor().getCorreo().equals(correoVendedor)) {
            throw new SecurityException("No tienes permiso para eliminar este producto");
        }

        // En lugar de eliminar físicamente, marcamos como inactivo
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public Producto bloquearProducto(Long id) {
        Producto producto = getProductoById(id);
        producto.setActivo(false);
        return productoRepository.save(producto);
    }

    @Override
    public Producto desbloquearProducto(Long id) {
        Producto producto = getProductoById(id);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }
}