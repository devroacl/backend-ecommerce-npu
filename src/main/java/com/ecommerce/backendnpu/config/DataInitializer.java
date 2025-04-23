package com.ecommerce.backendnpu.config;

import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        // Inicializar roles si no existen
        if (!rolRepository.existsById(Rol.ID_ADMIN)) {
            Rol rolAdmin = new Rol();
            rolAdmin.setId(Rol.ID_ADMIN);
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setDescripcion("Administrador con acceso total al sistema");
            rolRepository.save(rolAdmin);
        }

        if (!rolRepository.existsById(Rol.ID_VENDEDOR)) {
            Rol rolVendedor = new Rol();
            rolVendedor.setId(Rol.ID_VENDEDOR);
            rolVendedor.setNombre("VENDEDOR");
            rolVendedor.setDescripcion("Usuario que puede vender productos en la plataforma");
            rolRepository.save(rolVendedor);
        }

        if (!rolRepository.existsById(Rol.ID_COMPRADOR)) {
            Rol rolComprador = new Rol();
            rolComprador.setId(Rol.ID_COMPRADOR);
            rolComprador.setNombre("COMPRADOR");
            rolComprador.setDescripcion("Usuario que puede comprar productos en la plataforma");
            rolRepository.save(rolComprador);
        }
    }
}