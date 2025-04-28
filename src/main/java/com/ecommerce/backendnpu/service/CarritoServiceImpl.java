package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Carrito;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public Optional<Carrito> findByUsuario(Usuario usuario) {
        // Si usas Spring Data JPA, el repositorio debe definir el método:
        return carritoRepository.findByUsuario(usuario);
    }
    @Override
    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public void delete(Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public void vaciarCarrito(Usuario usuario) {
        Carrito carrito = findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}