package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario_Id(Long usuarioId); // Obtener pedidos de un usuario específico

    // Puedes agregar más métodos según las necesidades del negocio, por ejemplo:
    List<Pedido> findByEstadoPedido_Nombre(String estado); // Filtrar pedidos por estado
    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin); // Obtener pedidos en un rango de fechas


}
