package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.ItemsPedido;
import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.service.PedidoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoRestController {
    private final PedidoServiceImpl pedidoService;

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    // Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    // Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    // Obtener los pedidos de un usuario específico
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(usuarioId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    // Obtener los items de un pedido
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemsPedido>> obtenerItemsPedido(@PathVariable Long id) {
        List<ItemsPedido> items = pedidoService.obtenerItemsPedido(id);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Actualizar el estado de un pedido
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstadoPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> estado) {

        String nuevoEstado = estado.get("estado");
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pedido pedidoActualizado = pedidoService.actualizarEstadoPedido(id, nuevoEstado);
        return new ResponseEntity<>(pedidoActualizado, HttpStatus.OK);
    }

    // Eliminar un pedido por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

   @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        pedidoService.actualizarPedido(id);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }


}
