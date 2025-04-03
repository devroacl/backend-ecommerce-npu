package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha",nullable = false)
    public LocalDateTime fecha;

    //usuario-id

    @ManyToOne
    @JoinColumn(name = "usuarioId",nullable = false)
    private Usuario usuarioId;

    //Estadopedido (conectado con estado pedido)

    @ManyToOne
    @JoinColumn(name = "estadopedido",nullable = false)
    private EstadoPedido estadopedido;


}
