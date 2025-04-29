package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.exception.ProductoNotFoundException;
import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final GoogleCloudStorageService storageService;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository,
                               GoogleCloudStorageService storageService) {
        this.productoRepository = productoRepository;
        this.storageService = storageService;
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

    // Modificar el método save para manejar imágenes
    @Override
    public Producto save(Producto producto, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            String fileName = generarNombreUnico(imagen);
            storageService.uploadFile(imagen, fileName);
            producto.setImagen(fileName);
        }
        return productoRepository.save(producto);
    }

    @Override
    public void delete(Long id) {
        productoRepository.deleteById(id);
    }


    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id).map(producto -> {
            if (producto.getImagen() != null) {
                producto.setImagen(storageService.generateSignedUrl(producto.getImagen()));
            }
            return producto;
        });
    }

    private String generarNombreUnico(MultipartFile file) {
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
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