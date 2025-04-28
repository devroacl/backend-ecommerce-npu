package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.RolRepository;
import com.ecommerce.backendnpu.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    // Método para buscar un usuario por su ID
    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Método para obtener el usuario autenticado actualmente
    @Override
    public Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String correo = auth.getName();
            return findByCorreo(correo).orElse(null);
        }
        return null;
    }

    // Método para buscar un usuario por su correo electrónico
    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }





}

