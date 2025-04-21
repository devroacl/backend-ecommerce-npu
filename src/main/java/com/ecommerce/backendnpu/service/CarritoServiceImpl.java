package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;

    public CarritoServiceImpl(
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            ProductoRepository productoRepository,
            PedidoRepository pedidoRepository,
            ItemsPedidoRepository itemsPedidoRepository,
            EstadoPedidoRepository estadoPedidoRepository) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemsPedidoRepository = itemsPedidoRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    @Override
    public Carrito obtenerCarritoUsuario(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    nuevoCarrito.setFechaCreacion(LocalDateTime.now());
                    nuevoCarrito.setTotal(BigDecimal.ZERO);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    @Override
    @Transactional
    public Carrito agregarProducto(Usuario usuario, Long productoId, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Carrito carrito = obtenerCarritoUsuario(usuario);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

        // Validar stock disponible
        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("No hay suficiente stock disponible");
        }

        // Verificar si el producto ya está en el carrito
        Optional<ItemCarrito> existingItem = itemCarritoRepository.findByCarritoAndProducto(carrito, producto);

        if (existingItem.isPresent()) {
            // Actualizar cantidad si ya existe
            ItemCarrito item = existingItem.get();
            int nuevaCantidad = item.getCantidad() + cantidad;

            if (nuevaCantidad > producto.getStock()) {
                throw new IllegalArgumentException("No hay suficiente stock disponible");
            }

            item.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(item);
        } else {
            // Crear nuevo item si no existe
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);

            carrito.agregarItem(nuevoItem);
            itemCarritoRepository.save(nuevoItem);
        }

        // Actualizar fecha de modificación y total
        carrito.setUltimaModificacion(LocalDateTime.now());
        carrito.actualizarTotal();
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito actualizarCantidad(Usuario usuario, Long productoId, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Carrito carrito = obtenerCarritoUsuario(usuario);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

        // Validar stock disponible
        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("No hay suficiente stock disponible");
        }

        ItemCarrito item = itemCarritoRepository.findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);

        // Actualizar carrito
        carrito.setUltimaModificacion(LocalDateTime.now());
        carrito.actualizarTotal();
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void eliminarProducto(Usuario usuario, Long productoId) {
        Carrito carrito = obtenerCarritoUsuario(usuario);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

        ItemCarrito item = itemCarritoRepository.findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        carrito.eliminarItem(item);
        itemCarritoRepository.delete(item);

        // Actualizar carrito
        carrito.setUltimaModificacion(LocalDateTime.now());
        carrito.actualizarTotal();
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Usuario usuario) {
        Carrito carrito = obtenerCarritoUsuario(usuario);
        List<ItemCarrito> items = new ArrayList<>(carrito.getItems());

        for (ItemCarrito item : items) {
            carrito.eliminarItem(item);
        }

        itemCarritoRepository.deleteAll(items);
        carrito.setUltimaModificacion(LocalDateTime.now());
        carrito.setTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
    }

    @Override
    public List<ItemCarrito> obtenerItemsCarrito(Usuario usuario) {
        Carrito carrito = obtenerCarritoUsuario(usuario);
        return itemCarritoRepository.findByCarrito(carrito);
    }

    @Override
    @Transactional
    public Pedido procesarCompra(Usuario usuario) {
        Carrito carrito = obtenerCarritoUsuario(usuario);
        List<ItemCarrito> items = carrito.getItems();

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Verificar stock de todos los productos
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            if (item.getCantidad() > producto.getStock()) {
                throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre());
            }
        }

        // Buscar el estado inicial (pendiente)
        EstadoPedido estadoInicial = estadoPedidoRepository.findByNombreEstado("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado de pedido 'PENDIENTE' no configurado en el sistema"));

        // Crear el pedido
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setUsuario(usuario);
        pedido.setEstadoPedido(estadoInicial);

        // Guardar el pedido para obtener un ID
        pedido = pedidoRepository.save(pedido);

        // Crear items de pedido desde los items del carrito
        for (ItemCarrito itemCarrito : items) {
            Producto producto = itemCarrito.getProducto();

            // Crear el item del pedido
            ItemsPedido itemPedido = new ItemsPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProducto(producto);
            itemPedido.setCantidad(itemCarrito.getCantidad());
            itemPedido.setPreciounitario(producto.getPrecio());

            // Guardar el item del pedido
            itemsPedidoRepository.save(itemPedido);

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - itemCarrito.getCantidad());
            productoRepository.save(producto);
        }

        // Vaciar el carrito después de procesar la compra
        vaciarCarrito(usuario);

        return pedido;
    }
}