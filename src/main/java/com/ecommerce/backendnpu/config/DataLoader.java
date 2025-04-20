package com.ecommerce.backendnpu.config;

import com.ecommerce.backendnpu.model.ERol;
import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// Archivo: config/DataLoader.java
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            // Roles principales
            Rol admin = new Rol();
            admin.setNombre(ERol.ROLE_ADMIN);
            rolRepository.save(admin);

            // Roles de usuarios
            Arrays.asList(ERol.ROLE_COMPRADOR, ERol.ROLE_VENDEDOR).forEach(rol -> {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombre(rol);
                rolRepository.save(nuevoRol);
            });
        }
    }
}