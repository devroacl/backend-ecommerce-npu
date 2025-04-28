package com.ecommerce.backendnpu.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_carrito")
@Data
public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;
}