package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

@Entity
@Table(name= "resena")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    

}
