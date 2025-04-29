package com.ecommerce.backendnpu.Api;


import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.PedidoItem;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.service.PedidoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Obtener pedidos del comprador autenticado
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<List<Pedido>> getPedidosDelComprador() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario comprador = usuarioService.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Pedido> pedidos = pedidoService.findByComprador(comprador);
        return ResponseEntity.ok(pedidos);
    }

//----------Vendedor vea sus ventas------

    @GetMapping("/mis-ventas")
    @PreAuthorize("hasRole('ROLE_VENDEDOR')")
    public ResponseEntity<List<PedidoItem>> getVentasDelVendedor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario vendedor = usuarioService.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<PedidoItem> itemsVendidos = pedidoService.findPedidoItemsByVendedor(vendedor);
        return ResponseEntity.ok(itemsVendidos);
    }




}