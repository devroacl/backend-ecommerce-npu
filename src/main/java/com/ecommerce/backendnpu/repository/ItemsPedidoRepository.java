package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.ItemsPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsPedidoRepository extends JpaRepository<ItemsPedido, Long> {
    List<ItemsPedido> findByPedido_Id(Long pedidoId); // Convención para buscar por clave foránea

    // Otros métodos que podemos utilizar incluso Query
}
