package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByVendedor(Usuario vendedor);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);
    List<Producto> findByVendedorCorreo(String correoVendedor);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}