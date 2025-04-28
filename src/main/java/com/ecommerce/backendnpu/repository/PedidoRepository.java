package com.ecommerce.backendnpu.repository;


import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByComprador(Usuario comprador);
}