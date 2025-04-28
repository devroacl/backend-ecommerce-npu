package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.repository.ItemsPedidoRepository;
import com.ecommerce.backendnpu.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private  ItemsPedidoRepository itemsPedidoRepository;
    @Autowired
    private CarritoService carritoService; // Asegúrate de que está inyectado

    private ProductoService productoService;

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> findByComprador(Usuario comprador) {
        return pedidoRepository.findByComprador(comprador);
    }

    @Override
    public List<Pedido> findByVendedor(Usuario vendedor) {
        // Implementar lógica para encontrar pedidos que contengan productos de este vendedor
        List<Pedido> todosLosPedidos = pedidoRepository.findAll();
        return todosLosPedidos.stream()
                .filter(pedido -> tieneProductosDeVendedor(pedido, vendedor))
                .collect(Collectors.toList());
    }

    @Override
    public boolean tieneProductosDeVendedor(Pedido pedido, Usuario vendedor) {
        return pedido.getPedidoItems().stream()
                .anyMatch(item -> item.getProducto().getVendedor().getId().equals(vendedor.getId()));
    }
    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void delete(Long id) {
        pedidoRepository.deleteById(id);
    }

    // Logica para que el carrito de productos pase a pedido confirmado.
    @Override
    @Transactional
    public Pedido crearPedidoDesdeCarrito(Usuario usuario) {
        Carrito carrito = carritoService.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Validar stock y convertir a Pedido
        Pedido pedido = new Pedido();
        pedido.setComprador(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProducto(producto);
            pedidoItem.setCantidad(item.getCantidad());
            pedidoItem.setPreciounitario(producto.getPrecio()); // Corregido
            pedido.agregarItem(pedidoItem);

            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.save(producto);
        }

        pedido.calcularTotal(); // Método implementado

        return pedidoRepository.save(pedido);
    }

    @Override
    public List<PedidoItem> findPedidoItemsByVendedor(Usuario vendedor) {
        return itemsPedidoRepository.findByProducto_Vendedor(vendedor);
    }

}