package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> findAllRoles();

    Optional<Rol> findRolById(Long id);

    Optional<Rol> findRolByCargo(String cargo);

    Rol saveRol(Rol rol);

    void deleteRolById(Long id);
}
