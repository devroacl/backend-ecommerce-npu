package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Método corregido: Busca pedidos por el ID del usuario (Long)
    List<Pedido> findByUsuarioId(Long usuarioId); // ✅ Usa Long, no Usuario
}