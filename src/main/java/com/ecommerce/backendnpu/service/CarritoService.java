package com.ecommerce.backendnpu.service;



import com.ecommerce.backendnpu.model.Carrito;
import com.ecommerce.backendnpu.model.Usuario;

import java.util.Optional;

public interface CarritoService {
    Optional<Carrito> findByUsuario(Usuario usuario); // Retorna Optional
    Carrito save(Carrito carrito);
    void delete(Long id);
    void vaciarCarrito(Usuario usuario);
}