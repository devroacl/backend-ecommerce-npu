package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Ya hereda métodos como save(), findById(), findAll(), deleteById()

}
