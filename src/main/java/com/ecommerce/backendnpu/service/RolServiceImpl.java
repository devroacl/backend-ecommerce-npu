package com.ecommerce.backendnpu.service;

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
        return rolRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Rol guardarRol(Rol rol) {
        if (existeRolPorNombre(rol.getNombre())) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + rol.getNombre());
        }
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizarRol(Long id, Rol rol) {
        return rolRepository.findById(id)
                .map(rolExistente -> {
                    // Validar nombre duplicado (excepto para el mismo rol)
                    if (!rolExistente.getNombre().equals(rol.getNombre()) && existeRolPorNombre(rol.getNombre())) {
                        throw new RuntimeException("Ya existe un rol con el nombre: " + rol.getNombre());
                    }
                    rolExistente.setNombre(rol.getNombre());
                    rolExistente.setDescripcion(rol.getDescripcion());
                    return rolRepository.save(rolExistente);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public void eliminarRol(Long id) {
        if (id.equals(Rol.ID_ADMIN) || id.equals(Rol.ID_VENDEDOR) || id.equals(Rol.ID_COMPRADOR)) {
            throw new RuntimeException("No se pueden eliminar roles predefinidos");
        }
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRolPorNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }

    // Métodos para roles predefinidos
    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolAdmin() {
        return rolRepository.findById(Rol.ID_ADMIN)
                .orElseThrow(() -> new RuntimeException("Rol admin no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolVendedor() {
        return rolRepository.findById(Rol.ID_VENDEDOR)
                .orElseThrow(() -> new RuntimeException("Rol vendedor no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerRolComprador() {
        return rolRepository.findById(Rol.ID_COMPRADOR)
                .orElseThrow(() -> new RuntimeException("Rol comprador no encontrado"));
    }
}