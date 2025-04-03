package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "estadoPedido")
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombreestado",nullable = false,length =45)
    private String nombreEstado;


}
