package com.ecommerce.backendnpu.service;


import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;
    private final EstadoPedidoRepository estadoPedidoRepository; // Para gestionar los estados del pedido

    public PedidoServiceImpl(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository, ItemsPedidoRepository itemsPedidoRepository, EstadoPedidoRepository estadoPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.itemsPedidoRepository = itemsPedidoRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    @Override
    public List<Pedido> findAllPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> findPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> findPedidosByUsuarioId(Long usuarioId) {
        return pedidoRepository.findByUsuario_Id(usuarioId);
    }

    @Override
    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deletePedidoById(Long id) {
        pedidoRepository.deleteById(id);
    }

    /// falta logica para crear nuevo pedido

    @Override
    public Pedido crearNuevoPedido(Long usuarioId, List<Long> productoIdsConCantidades) {
        return null;
    }

    //Falta logica para actualizar pedido.
    @Override
    public void actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {

    }

}
