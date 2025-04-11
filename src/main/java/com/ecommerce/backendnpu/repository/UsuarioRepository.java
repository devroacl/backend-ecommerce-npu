package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByRut(String rut);

    Optional<Usuario> findByToken(String token);
}