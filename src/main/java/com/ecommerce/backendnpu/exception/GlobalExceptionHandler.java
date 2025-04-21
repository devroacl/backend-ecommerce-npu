package com.ecommerce.backendnpu.exception;

// Archivo: GlobalExceptionHandler.java

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductoNotFoundException(ProductoNotFoundException e) {
        return e.getMessage(); // Devuelve el mensaje de error con status 404
    }
}