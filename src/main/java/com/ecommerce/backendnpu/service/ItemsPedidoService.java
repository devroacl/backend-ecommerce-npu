package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.ItemsPedido;

import java.util.List;
import java.util.Optional;

public interface ItemsPedidoService {

    List<ItemsPedido> findAllItemsPedido(); // Para obtener todos los items
    Optional<ItemsPedido> findItemsPedidoById(Long id); // Para obtener un item por su ID
    List<ItemsPedido> findItemsPedidoByPedidoId(Long pedidoId); // Para obtener items por el ID del pedido
    ItemsPedido saveItemsPedido(ItemsPedido itemPedido);
    void deleteItemsPedidoById(Long id);

}
