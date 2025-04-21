package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.ItemsPedido;

import java.util.List;
import java.util.Optional;

public interface ItemsPedidoService {
    List<ItemsPedido> findAllItemsPedido();

    Optional<ItemsPedido> findItemsPedidoById(Long id);

    ItemsPedido saveItemsPedido(ItemsPedido itemPedido);

    void deleteItemsPedidoById(Long id);
}