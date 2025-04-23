package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.RolRepository;
import com.ecommerce.backendnpu.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findUsuarioByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findUsuarioByRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findUsuarioByToken(String token) {
        return usuarioRepository.findByToken(token);
    }

    @Override
    @Transactional
    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deleteUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario registrarNuevoUsuario(Usuario usuario, String tipoRol) {
        // Validar que el correo y rut no existan
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new RuntimeException("El RUT ya está registrado");
        }

        // Asignar rol según el tipo seleccionado
        Rol rol;
        switch (tipoRol.toUpperCase()) {
            case "COMPRADOR":
                rol = rolRepository.findById(Rol.ID_COMPRADOR)
                        .orElseThrow(() -> new RuntimeException("Rol comprador no encontrado"));
                break;
            case "VENDEDOR":
                rol = rolRepository.findById(Rol.ID_VENDEDOR)
                        .orElseThrow(() -> new RuntimeException("Rol vendedor no encontrado"));
                break;
            case "ADMIN":
                rol = rolRepository.findById(Rol.ID_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Rol admin no encontrado"));
                break;
            default:
                throw new RuntimeException("Tipo de rol no válido");
        }

        usuario.setRol(rol);
        usuario.setVerificar(false);
        usuario.setToken(UUID.randomUUID().toString());
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        return usuarioRepository.save(usuario);
    }

    // Método añadido para resolver el error
    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }
}