package com.ecommerce.backendnpu.Api;
//ProductoRestController es el punto de entrada de tu API REST
// para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la entidad Producto.
// Cada uno de los métodos del controlador está mapeado a una operación CRUD
// a través de las anotaciones de Spring MVC (@GetMapping, @PostMapping, @DeleteMapping, @PutMapping)
// y las rutas de los endpoints.


import com.ecommerce.backendnpu.model.Producto;
import com.ecommerce.backendnpu.service.ProductoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//Si no funciona RequiredArgs es porque no lee la libreria.En este caso lombok.Se debe agregar a dependencias en pom.
@RestController
@RequiredArgsConstructor // Ayuda con el constructor
public class ProductoRestController {

    //Inyección de dependencia  ---recordatorio para mi misma.Agrega primero este constructor para luego llamar a propiedades de la libreria.
    private final ProductoServiceImpl productoService;


    @GetMapping("/producto/{id}")
    public Producto findById(@PathVariable Long id) {
        return productoService.findById(id); // Aquí se accede a productoService
    }










}
