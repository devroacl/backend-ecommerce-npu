package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name= "resena")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "puntaje", nullable = false)
    private BigDecimal puntaje;
    


}
