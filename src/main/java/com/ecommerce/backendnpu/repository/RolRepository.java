package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Devuelve un Optional para manejar posibles valores nulos
    Optional<Rol> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}