package com.ecommerce.backendnpu.service;


import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.PedidoItem;
import com.ecommerce.backendnpu.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);

    List<Pedido> findByVendedor(Usuario vendedor);
    boolean tieneProductosDeVendedor(Pedido pedido, Usuario vendedor);
    Pedido save(Pedido pedido);
    void delete(Long id);
    Pedido crearPedidoDesdeCarrito(Usuario usuario);


    List<Pedido> findByComprador(Usuario comprador);
    List<PedidoItem> findPedidoItemsByVendedor(Usuario vendedor);
}