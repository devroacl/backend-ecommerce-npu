package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Carrito;
import com.ecommerce.backendnpu.model.ItemCarrito;
import com.ecommerce.backendnpu.model.Pedido;
import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CarritoService {
    Carrito obtenerCarritoUsuario(Usuario usuario);
    Carrito agregarProducto(Usuario usuario, Long productoId, Integer cantidad);
    Carrito actualizarCantidad(Usuario usuario, Long productoId, Integer cantidad);
    void eliminarProducto(Usuario usuario, Long productoId);
    void vaciarCarrito(Usuario usuario);
    List<ItemCarrito> obtenerItemsCarrito(Usuario usuario);
    Pedido procesarCompra(Usuario usuario);
}