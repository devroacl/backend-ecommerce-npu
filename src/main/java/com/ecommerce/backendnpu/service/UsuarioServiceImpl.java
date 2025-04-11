package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.RolRepository;
import com.ecommerce.backendnpu.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
   // private final BCryptPasswordEncoder passwordEncoder; // Asegúrate de tener esto configurado como un Bean

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
       // this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findUsuarioByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public Optional<Usuario> findUsuarioByRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }

    @Override
    public Optional<Usuario> findUsuarioByToken(String token) {
        return usuarioRepository.findByToken(token);
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Modificar metodo cuando se agregen mas cosas.Queda con retorno null por mientras para que no cause errores en la clase.
    @Override
    public Usuario registrarNuevoUsuario(Usuario usuario, String cargoRol) {
        return null;
    }

    }

