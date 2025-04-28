package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.service.CarritoService;
import com.ecommerce.backendnpu.service.PedidoService;
import com.ecommerce.backendnpu.service.ProductoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoRestController {

    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    @Autowired
    public CarritoRestController(CarritoService carritoService, ProductoService productoService,
                                 UsuarioService usuarioService, PedidoService pedidoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
    }

    @PostMapping("/confirmar")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> confirmarPedido() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        Carrito carrito = carritoService.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Carrito vacío"));
        }

        // Validar stock
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Stock insuficiente para: " + producto.getNombre()));
            }
        }

        // Crear Pedido
        Pedido pedido = new Pedido();
        pedido.setComprador(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        // Convertir CarritoItems a PedidoItems y actualizar stock
        for (CarritoItem item : carrito.getItems()) {
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProducto(item.getProducto());
            pedidoItem.setCantidad(item.getCantidad());
            pedidoItem.setPreciounitario(item.getProducto().getPrecio());
            pedido.agregarItem(pedidoItem);

            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.save(producto);
        }

        pedido.calcularTotal();
        pedidoService.save(pedido);
        carritoService.vaciarCarrito(usuario);

        return ResponseEntity.ok(pedido);
    }
}