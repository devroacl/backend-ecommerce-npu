package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAllUsuarios();

    Optional<Usuario> findUsuarioById(Long id);

    Optional<Usuario> findUsuarioByCorreo(String correo);

    Optional<Usuario> findUsuarioByRut(String rut);

    Optional<Usuario> findUsuarioByToken(String token);

    Usuario saveUsuario(Usuario usuario);

    void deleteUsuarioById(Long id);

    // Método específico para el registro de un nuevo usuario
    Usuario registrarNuevoUsuario(Usuario usuario, String cargoRol);
}
