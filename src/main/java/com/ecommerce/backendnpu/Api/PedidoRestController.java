package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.ERol;
import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.PedidoItem;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.PedidoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;



@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    @Autowired
    public PedidoRestController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mis-ventas")
    public ResponseEntity<List<PedidoItem>> getVentasDelVendedor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar rol manualmente
        if (!usuario.getRol().getNombre().equals(ERol.ROLE_VENDEDOR)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        List<PedidoItem> itemsVendidos = pedidoService.findPedidoItemsByVendedor(usuario);
        return ResponseEntity.ok(itemsVendidos);
    }

    // PedidoRestController.java
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<Pedido>> getPedidosDelComprador() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar rol manualmente
        if (!usuario.getRol().getNombre().equals(ERol.ROLE_COMPRADOR)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        List<Pedido> pedidos = pedidoService.findByComprador(usuario);
        return ResponseEntity.ok(pedidos);
    }

    // Manejo de excepciones
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUsuarioNoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());



    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }


}