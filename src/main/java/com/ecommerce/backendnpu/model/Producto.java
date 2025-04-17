package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "producto")
@Data // Genera getters, setters, toString, etc.
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con TODOS los campos (opcional)
@Builder   // para crear objetos de manera flexible.
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



        // Constructor personalizado para campos básicos---para que en test que pide 3 campos
        public Producto(Long id, String nombre, Double precio) {
                this.id = id;
                this.nombre = nombre;
                this.precio = precio;
        }

//Contructor para que producto tenga nombre,id,
    public Producto(long l, String mouse, double v, long l1) {
    }

    public Producto(String laptop, double v) {
    }

}