package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

//Extensión de JpaRepository ---tu interfaz ProductoRepository automáticamente hereda métodos para realizar las operaciones CRUD básicas (crear, leer, actualizar, eliminar) en la entidad Producto, utilizando el tipo Long como el tipo de la clave primaria de la entidad Producto

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //Por el momento ProductoRepository existe para JpaRepository permita el CRUD
    //Se puede agregar consultas Query aqui pero no es el caso por el momento.

}


/* su funcionalidad principal es proporcionar las operaciones CRUD básicas heredadas de JpaRepository.
Pero su diseño permite una gran flexibilidad para añadir consultas personalizadas
según las necesidades de tu aplicación. Como el uso de @Query*/

