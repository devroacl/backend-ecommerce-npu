package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido crearPedido(Pedido pedido);
    Pedido obtenerPedidoPorId(Long id);
    List<Pedido> obtenerTodosLosPedidos();
    void eliminarPedido(Long id);
    Pedido actualizarPedido(Long id);
}

