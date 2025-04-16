package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Buscar rol por nombre
    Rol findByNombre(String nombre);

    // Verificar si existe un rol por nombre
    boolean existsByNombre(String nombre);
}