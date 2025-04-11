package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.EstadoPedido;
import com.ecommerce.backendnpu.repository.EstadoPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoPedidoServiceImpl implements EstadoPedidoService {
    private final EstadoPedidoRepository estadoPedidoRepository;

    public EstadoPedidoServiceImpl(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    public List<EstadoPedido> findAllEstadosPedido() {
        return estadoPedidoRepository.findAll();
    }

    public Optional<EstadoPedido> findEstadoPedidoById(Long id) {
        return estadoPedidoRepository.findById(id);
    }

    @Override
    public Optional<EstadoPedido> findEstadoPedidoByNombre(String nombre) {
        return estadoPedidoRepository.findByNombre(nombre);
    }

    public EstadoPedido saveEstadoPedido(EstadoPedido estadoPedido) {
        return estadoPedidoRepository.save(estadoPedido);
    }

    public void deleteEstadoPedidoById(Long id) {
        estadoPedidoRepository.deleteById(id);
    }

}
