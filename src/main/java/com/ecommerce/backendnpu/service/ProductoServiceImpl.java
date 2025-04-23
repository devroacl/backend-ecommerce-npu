package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import com.ecommerce.backendnpu.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto updateProducto(Long id, Producto producto) {
        // Verificar que el producto existe
        getProductoById(id);
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        // Verificar que el producto existe
        getProductoById(id);
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> searchProductos(String nombre) {
        // Implementación básica, idealmente usar un método del repositorio
        return productoRepository.findAll().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> filterProductosByCategoria(Integer categoriaId) {
        // Implementación básica, idealmente usar un método del repositorio
        return productoRepository.findAll().stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().equals(categoriaId))
                .collect(Collectors.toList());
    }
}