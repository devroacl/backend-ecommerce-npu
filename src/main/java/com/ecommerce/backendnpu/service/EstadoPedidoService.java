package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.EstadoPedido;

import java.util.List;
import java.util.Optional;

public interface EstadoPedidoService {
    List<EstadoPedido> findAllEstadosPedido();
    Optional<EstadoPedido> findEstadoPedidoById(Long id);
    Optional<EstadoPedido> findEstadoPedidoByNombre(String nombre);
    EstadoPedido saveEstadoPedido(EstadoPedido estadoPedido);
    void deleteEstadoPedidoById(Long id);
}
