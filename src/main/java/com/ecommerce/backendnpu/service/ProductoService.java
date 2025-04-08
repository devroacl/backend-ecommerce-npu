package com.ecommerce.backendnpu.service;

//Contiene la lógica de negocio específica de la aplicación. Define "qué" operaciones
// se pueden realizar con las entidades (en este caso, Producto) y cómo se realizan.

//

import com.ecommerce.backendnpu.model.Producto;

import java.util.List;

public interface ProductoService {

    //En la inteface conjunto de métodos que las clases deben implementar.
    //Declarar metodos o aciones que se puede hacer en producto CRUD


    //Para que no cause error usa @Override en ProductoServiceImpl sobrescribiendo estos metodos.

    Producto findById(Long id); // Leer-Read

    List<Producto> findAll(); //Read

    Producto save(Producto producto); // Crear (Create)(Si crea un nuevo registro) o Actualizar (Update)(Si actualiza un registro ya existente)

    void deleteProductoById(Long id);  //Eliminar (Delete)



}
