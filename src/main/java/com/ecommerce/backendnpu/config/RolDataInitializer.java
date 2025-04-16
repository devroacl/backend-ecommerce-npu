package com.ecommerce.backendnpu.config;

import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RolDataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;

    public RolDataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear rol de comprador si no existe
        if (!rolRepository.existsById(Rol.ID_COMPRADOR)) {
            Rol comprador = new Rol();
            comprador.setId(Rol.ID_COMPRADOR);
            comprador.setNombre("COMPRADOR");
            comprador.setDescripcion("Usuario con permisos para comprar productos");
            rolRepository.save(comprador);
        }

        // Crear rol de vendedor si no existe
        if (!rolRepository.existsById(Rol.ID_VENDEDOR)) {
            Rol vendedor = new Rol();
            vendedor.setId(Rol.ID_VENDEDOR);
            vendedor.setNombre("VENDEDOR");
            vendedor.setDescripcion("Usuario con permisos para vender productos");
            rolRepository.save(vendedor);
        }
    }
}