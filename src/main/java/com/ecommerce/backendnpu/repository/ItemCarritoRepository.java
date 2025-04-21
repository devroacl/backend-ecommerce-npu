package com.ecommerce.backendnpu.repository;
import com.ecommerce.backendnpu.model.Carrito;
import com.ecommerce.backendnpu.model.ItemCarrito;
import com.ecommerce.backendnpu.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByCarrito(Carrito carrito);
    Optional<ItemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
}