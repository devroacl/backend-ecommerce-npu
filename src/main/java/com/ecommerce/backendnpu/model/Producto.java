package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nombre", nullable = false, length = 40)
        private String nombre;

        @Column( name = "precio", nullable = false)
        private int precio;

        @Column( name = "cantidad", nullable = false)
        private int cantidad;

        @ManyToOne
        @JoinColumn(name = "categoria_id" , nullable = true)
        private Categoria categoria;

    }


