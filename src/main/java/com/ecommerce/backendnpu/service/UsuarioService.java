package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Usuario;

import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> findById(Long id);
    Usuario getUsuarioAutenticado();
    Optional<Usuario> findByCorreo(String correo);
}