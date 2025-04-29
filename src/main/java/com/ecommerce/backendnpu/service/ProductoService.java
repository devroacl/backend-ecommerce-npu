package com.ecommerce.backendnpu.service;


import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

        List<Producto> findAll();
        List<Producto> findByCategoria(Categoria categoria);
        List<Producto> findByVendedor(Usuario vendedor);
        List<Producto> findByNombreContaining(String nombre);
        Producto save(Producto producto, MultipartFile imagen);
        void delete(Long id);
        Optional<Producto> findById(Long id);
        List<Producto> findByActivoTrue();
        List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);
        List<Producto> findByVendedorCorreo(String correoVendedor);
        Producto actualizarProducto(Long id, Producto productoActualizado, String correoVendedor);
        void eliminarProducto(Long id, String correoVendedor);
        Producto bloquearProducto(Long id);
        Producto desbloquearProducto(Long id);
    List<Producto> findByNombreContainingAndActivoTrue(String nombre);
    }


