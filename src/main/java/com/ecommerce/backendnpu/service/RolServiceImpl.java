package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.ERol;
import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // --- Métodos base ---
    @Override
    @Transactional(readOnly = true)
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolPorNombre(String nombre) {
        // Convertir String a ERol
        ERol nombreRol = ERol.valueOf(nombre.toUpperCase());
        return rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre));
    }

    @Override
    @Transactional
    public Rol guardarRol(Rol rol) {
        if (existeRolPorNombre(rol.getNombre().name())) {
            throw new RuntimeException("Rol ya existe: " + rol.getNombre());
        }
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizarRol(Long id, Rol rol) {
        return rolRepository.findById(id)
                .map(rolExistente -> {
                    // Validar nombre único
                    if (!rolExistente.getNombre().equals(rol.getNombre()) && existeRolPorNombre(rol.getNombre().name())) {
                        throw new RuntimeException("Nombre de rol ya existe");
                    }
                    rolExistente.setNombre(rol.getNombre());
                    return rolRepository.save(rolExistente);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void eliminarRol(Long id) {
        if (rolRepository.findById(id).map(Rol::getNombre).orElseThrow().toString().startsWith("ROLE_")) {
            throw new RuntimeException("No se pueden eliminar roles predefinidos");
        }
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRolPorNombre(String nombre) {
        ERol nombreRol = ERol.valueOf(nombre.toUpperCase());
        return rolRepository.existsByNombre(nombreRol);
    }

    // --- Métodos para roles predefinidos ---
    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolAdmin() {
        return rolRepository.findByNombre(ERol.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no configurado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolVendedor() {
        return rolRepository.findByNombre(ERol.ROLE_VENDEDOR)
                .orElseThrow(() -> new RuntimeException("Rol VENDEDOR no configurado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolComprador() {
        return rolRepository.findByNombre(ERol.ROLE_COMPRADOR)
                .orElseThrow(() -> new RuntimeException("Rol COMPRADOR no configurado"));
    }
}