package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.repository.EstadoPedidoRepository;
import com.ecommerce.backendnpu.repository.ItemsPedidoRepository;
import com.ecommerce.backendnpu.repository.PedidoRepository;
import com.ecommerce.backendnpu.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             UsuarioRepository usuarioRepository,
                             ItemsPedidoRepository itemsPedidoRepository,
                             EstadoPedidoRepository estadoPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.itemsPedidoRepository = itemsPedidoRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        Long usuarioId = pedido.getUsuario().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));
        pedido.setUsuario(usuario);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    @Override
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        pedidoExistente.setTotal(pedidoActualizado.getTotal());
        pedidoExistente.setEstadoPedido(pedidoActualizado.getEstadoPedido());
        return pedidoRepository.save(pedidoExistente);
    }

    @Override
    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        // Ahora usa directamente el ID sin necesidad de buscar el Usuario
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<ItemsPedido> obtenerItemsPedido(Long pedidoId) {
        return itemsPedidoRepository.findByPedido_Id(pedidoId);
    }

    @Override
    public Pedido actualizarEstadoPedido(Long id, String nuevoEstadoNombre) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        EstadoPedido nuevoEstado = estadoPedidoRepository.findByNombreEstado(nuevoEstadoNombre)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + nuevoEstadoNombre));
        pedido.setEstadoPedido(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}