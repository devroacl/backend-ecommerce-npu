package com.ecommerce.backendnpu.testdata;

import com.ecommerce.backendnpu.model.Producto;

import java.util.List;

public class TestData {

    //Para la entidad Producto

    public static List<Producto> getSampleProducts() {
        return List.of(
                new Producto(1L, "Laptop", 999.99),
                new Producto(2L, "Mouse", 25.50),
                new Producto(3L, "Teclado", 75.00),
                new Producto(4L, "Monitor", 200.00),
                new Producto(5L, "Auriculares", 59.99)
        );
    }

    // Repite para otras tablas si las tienes


}
