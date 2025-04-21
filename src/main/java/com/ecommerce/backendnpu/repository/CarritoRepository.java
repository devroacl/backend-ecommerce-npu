package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Carrito;
import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario(Usuario usuario);
}