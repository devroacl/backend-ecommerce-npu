package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    //Inyección de dependencia se realiza gracias a la anotación de Lombok
    private final ProductoRepository productoRepository;


    // Se debe modificar el retorno de estos metodos,Los metodos estan por defecto por el momento.excepto el metodo borrar,ese esta bien.

    @Override
    public Producto findById(Long id) {
        return null;
    }

    @Override
    public List<Producto> findAll() {
        return List.of();
    }

    @Override
    public Producto save(Producto producto) {
        return null;
    }

    @Override
    public void deleteProductoById(Long id) {

    }
}
