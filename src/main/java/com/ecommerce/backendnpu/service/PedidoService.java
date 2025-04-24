package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.ItemsPedido;
import com.ecommerce.backendnpu.model.Pedido;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PedidoService {
    Pedido crearPedido(Pedido pedido);
    Pedido obtenerPedidoPorId(Long id);
    List<Pedido> obtenerTodosLosPedidos();
    void eliminarPedido(Long id);
    Pedido actualizarPedido(Long id, Pedido pedidoActualizado);
    List<Pedido> obtenerPedidosPorUsuario(Long usuarioId);
    List<ItemsPedido> obtenerItemsPedido(Long pedidoId);
    Pedido actualizarEstadoPedido(Long id, String nuevoEstado);


}

