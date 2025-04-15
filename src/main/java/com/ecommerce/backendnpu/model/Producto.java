package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "producto")
@Data
public class Producto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "nombre", length = 45, nullable = false)
        private String nombre;

        @Column(name = "descripcion", length = 255)
        private String descripcion;

        @Column(name = "precio")
        private Double precio;

        @Column(name = "cantidad")
        private Integer cantidad;

        @ManyToOne
        @JoinColumn(name = "categoria_id",nullable = true)
        private Categoria categoria;





}