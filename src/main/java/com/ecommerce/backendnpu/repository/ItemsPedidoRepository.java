package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.ItemsPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsPedidoRepository extends JpaRepository<ItemsPedido, Long> {
    Long id(Long id);
    // Otros métodos que podemos utilizar incluso Query
}
