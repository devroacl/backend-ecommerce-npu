package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> obtenerTodosLosRoles();
    Optional<Rol> obtenerRolPorId(Long id);
    Rol obtenerRolPorNombre(String nombre);
    Rol guardarRol(Rol rol);
    Rol actualizarRol(Long id, Rol rol);
    void eliminarRol(Long id);
    boolean existeRolPorNombre(String nombre);

    // Métodos para roles predefinidos (¡DEBEN ESTAR AQUÍ!)
    Rol obtenerRolComprador();
    Rol obtenerRolVendedor();
    Rol obtenerRolAdmin();

}