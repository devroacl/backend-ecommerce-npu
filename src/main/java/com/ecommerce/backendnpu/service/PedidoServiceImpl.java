package com.ecommerce.backendnpu.service;


import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        // Obtener el usuario desde la base de datos usando el ID
        Long usuarioId = pedido.getUsuarioId().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        pedido.setUsuarioId(usuario);
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
    public Pedido actualizarPedido(Long id) {
        return null;
    }
}
