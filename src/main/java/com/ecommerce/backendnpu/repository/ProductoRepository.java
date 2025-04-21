package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Métodos para filtrar productos activos
    List<Producto> findByActivoTrue();
    Optional<Producto> findByIdAndActivoTrue(Long id);

    // Métodos para filtrar por categoría
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    // Métodos para vendedores
    List<Producto> findByVendedorCorreo(String correoVendedor);

    // Métodos adicionales para búsquedas
    List<Producto> findByNombreContainingAndActivoTrue(String nombre);
    List<Producto> findByPrecioBetweenAndActivoTrue(Double precioMin, Double precioMax);
}