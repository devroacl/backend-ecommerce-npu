package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.PedidoItem;

import java.util.List;
import java.util.Optional;

public interface ItemsPedidoService {
    List<PedidoItem> findAllItemsPedido();

    Optional<PedidoItem> findItemsPedidoById(Long id);

    PedidoItem saveItemsPedido(PedidoItem itemPedido);

    void deleteItemsPedidoById(Long id);
}