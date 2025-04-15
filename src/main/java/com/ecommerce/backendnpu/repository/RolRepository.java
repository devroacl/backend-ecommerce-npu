package com.ecommerce.backendnpu.repository;
import com.ecommerce.backendnpu.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

    @Repository
    public interface RolRepository extends JpaRepository<Rol, Long> {

        Optional<Rol> findByCargo(String cargo); // Para buscar un rol por su nombre (ej., "vendedor")
    }

