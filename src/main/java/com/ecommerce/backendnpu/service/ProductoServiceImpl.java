package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.exception.ProductoNotFoundException;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;


    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;

    }

    // Implementar todos los métodos de la interfaz

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> findByCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public List<Producto> findByVendedor(Usuario vendedor) {
        return productoRepository.findByVendedor(vendedor);
    }

    @Override
    public List<Producto> findByNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void delete(Long id) {
        productoRepository.deleteById(id);
    }

    //Arreglar el
    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> findByActivoTrue() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public List<Producto> findByCategoriaAndActivoTrue(Categoria categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Override
    public List<Producto> findByVendedorCorreo(String correoVendedor) {
        return productoRepository.findByVendedorCorreo(correoVendedor);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto productoActualizado, String correoVendedor) {
        return null;
    }

    @Override
    public void eliminarProducto(Long id, String correoVendedor) {

    }

    @Override
    public Producto bloquearProducto(Long id) {
        return null;
    }

    @Override
    public Producto desbloquearProducto(Long id) {
        return null;
    }


    @Override
    public List<Producto> findByNombreContainingAndActivoTrue(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }
}