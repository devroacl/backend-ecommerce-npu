package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.EstadoPedido;
import com.ecommerce.backendnpu.model.ItemsPedido;
import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.Usuario;
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
        // Obtener el usuario desde la base de datos usando el ID
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
    public Pedido actualizarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        // Lógica para actualizar el pedido

        return pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        return pedidoRepository.findByUsuarioId(usuario);
    }

    @Override
    public List<ItemsPedido> obtenerItemsPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + pedidoId));

        // Asumiendo que tienes un método en el repositorio de ItemsPedido
        return itemsPedidoRepository.findByPedido_Id(pedidoId);
    }

    @Override
    public Pedido actualizarEstadoPedido(Long id, String nuevoEstadoNombre) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        // Buscar el estado por su nombre (asumiendo que hay un método para buscar por nombre)
        EstadoPedido nuevoEstado = estadoPedidoRepository.findByNombreEstado(nuevoEstadoNombre)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con nombre: " + nuevoEstadoNombre));

        // Usar el método correcto según el modelo
        pedido.setEstadoPedido(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}