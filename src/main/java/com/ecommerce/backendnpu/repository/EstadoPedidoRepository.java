package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Long> {

    List<EstadoPedido> findAllEstadosPedido();
    Optional<EstadoPedido> findEstadoPedidoById(Long id);
    EstadoPedido saveEstadoPedido(EstadoPedido estadoPedido);
    void deleteEstadoPedidoById(Long id);
    Optional<EstadoPedido> findByNombre(String nombre);
}

