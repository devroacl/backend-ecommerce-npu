package com.ecommerce.backendnpu.Api;
import com.ecommerce.backendnpu.model.Carrito;
import com.ecommerce.backendnpu.model.ItemCarrito;
import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.UsuarioRepository;
import com.ecommerce.backendnpu.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carrito")
public class CarritoRestController {

    private final CarritoService carritoService;
    private final UsuarioRepository usuarioRepository;

    // Obtener el carrito del usuario actual
    @GetMapping
    public ResponseEntity<Carrito> obtenerCarrito(@RequestParam Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Carrito carrito = carritoService.obtenerCarritoUsuario(usuario);
        return ResponseEntity.ok(carrito);
    }

    // Agregar un producto al carrito
    @PostMapping("/agregar")
    public ResponseEntity<Carrito> agregarProducto(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Carrito carrito = carritoService.agregarProducto(usuario, productoId, cantidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    // Actualizar la cantidad de un producto en el carrito
    @PutMapping("/actualizar")
    public ResponseEntity<Carrito> actualizarCantidad(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Carrito carrito = carritoService.actualizarCantidad(usuario, productoId, cantidad);
        return ResponseEntity.ok(carrito);
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/eliminar")
    public ResponseEntity<Void> eliminarProducto(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        carritoService.eliminarProducto(usuario, productoId);
        return ResponseEntity.noContent().build();
    }

    // Vaciar el carrito
    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@RequestParam Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        carritoService.vaciarCarrito(usuario);
        return ResponseEntity.noContent().build();
    }

    // Ver los productos en el carrito
    @GetMapping("/items")
    public ResponseEntity<List<ItemCarrito>> obtenerItemsCarrito(@RequestParam Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        List<ItemCarrito> items = carritoService.obtenerItemsCarrito(usuario);
        return ResponseEntity.ok(items);
    }

    // Procesar la compra (convertir el carrito en un pedido)
    @PostMapping("/checkout")
    public ResponseEntity<Pedido> procesarCompra(@RequestParam Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Pedido pedido = carritoService.procesarCompra(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
}
