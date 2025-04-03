package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cargo",nullable = false,length =40)
    private String cargo;



}
