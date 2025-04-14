package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findByCategoriaId(Integer categoriaId);

    List<Producto> findByCategoriaId(Long categoriaId);


}