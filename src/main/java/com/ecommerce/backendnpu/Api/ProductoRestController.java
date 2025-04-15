package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.repository.ProductoRepository;
import com.ecommerce.backendnpu.service.ProductoService;
import com.ecommerce.backendnpu.service.ProductoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producto")
public class ProductoRestController {

    private final ProductoServiceImpl productoService;

    @GetMapping("/id/{id}")
    public ResponseEntity<Producto> findById(@PathVariable Long id) {
        return productoService.getProductoById(id)
                .map(ResponseEntity::ok) // Si el Optional contiene un Producto, devuelve ResponseEntity con status 200 (OK)
                .orElse(ResponseEntity.notFound().build()); // Si el Optional está vacío, devuelve ResponseEntity con status 404 (Not Found)
    }
}