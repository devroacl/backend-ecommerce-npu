package com.ecommerce.backendnpu.service;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto updateProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> searchProductos(String nombre) {
        return productoRepository.findByNombreContaining(nombre);
    }

    @Override
    public List<Producto> filterProductosByCategoria(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
}