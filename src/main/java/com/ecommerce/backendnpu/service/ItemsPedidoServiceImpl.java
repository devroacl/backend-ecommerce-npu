package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.PedidoItem;
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
    public List<PedidoItem> findAllItemsPedido() {
        return itemsPedidoRepository.findAll();
    }

    @Override
    public Optional<PedidoItem> findItemsPedidoById(Long id) {
        return itemsPedidoRepository.findById(id);
    }

    @Override
    public PedidoItem saveItemsPedido(PedidoItem itemPedido) {
        return itemsPedidoRepository.save(itemPedido);
    }

    @Override
    public void deleteItemsPedidoById(Long id) {
        itemsPedidoRepository.deleteById(id);
    }


}
