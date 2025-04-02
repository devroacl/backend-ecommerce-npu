package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import jdk.jfr.Description;

import java.math.BigDecimal;
import java.time.DateTimeException;

@Entity
@Table(name= "resena")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "puntaje", nullable = false)
    private BigDecimal puntaje;

    @Column(name = "descripcion", length = 80, nullable = true)
    private String descripcion;

    @Column(name= "fecha", nullable = false )
    private DateTimeException fecha;




}
