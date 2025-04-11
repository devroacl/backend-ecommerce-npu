package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.ItemsPedido;
import com.ecommerce.backendnpu.repository.ItemsPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemsPedidoServiceImpl implements ItemsPedidoService {
    private final ItemsPedidoRepository itemsPedidoRepository;

    public ItemsPedidoServiceImpl(ItemsPedidoRepository itemsPedidoRepository) {
        this.itemsPedidoRepository = itemsPedidoRepository;
    }

    @Override
    public List<ItemsPedido> findAllItemsPedido() {
        return itemsPedidoRepository.findAll();
    }

    @Override
    public Optional<ItemsPedido> findItemsPedidoById(Long id) {
        return itemsPedidoRepository.findById(id);
    }

    @Override
    public List<ItemsPedido> findItemsPedidoByPedidoId(Long pedidoId) {
        // Llama al método del repositorio que busca por pedidoId
        return itemsPedidoRepository.findByPedido_Id(pedidoId);
    }

    @Override
    public ItemsPedido saveItemsPedido(ItemsPedido itemPedido) {
        return itemsPedidoRepository.save(itemPedido);
    }

    @Override
    public void deleteItemsPedidoById(Long id) {
        itemsPedidoRepository.deleteById(id);
    }


}
