package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> findAllPedidos();

    Optional<Pedido> findPedidoById(Long id);

    List<Pedido> findPedidosByUsuarioId(Long usuarioId);

    Pedido savePedido(Pedido pedido); // Para crear un nuevo pedido o actualizar uno existente

    void deletePedidoById(Long id);

    // Métodos adicionales específicos de la lógica de negocio del ecommerce:
    Pedido crearNuevoPedido(Long usuarioId, List<Long> productoIdsConCantidades); // Ejemplo de método para crear un pedido

    void actualizarEstadoPedido(Long pedidoId, String nuevoEstado);
    // Otros métodos relacionados con la gestión de pedidos (cancelar, etc.)
}

