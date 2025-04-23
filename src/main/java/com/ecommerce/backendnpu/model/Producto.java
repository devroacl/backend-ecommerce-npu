package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Setter
@Getter
@Data
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
        @JoinColumn(name = "categoria_id", nullable = true)
        private Categoria categoria;

        // Constructor personalizado para campos básicos
        public Producto(Long id, String nombre, Double precio) {
                this.id = id;
                this.nombre = nombre;
                this.precio = precio;
        }

        // Constructor para productos con categoría
        public Producto(long id, String nombre, double precio, long categoriaId) {
                this.id = id;
                this.nombre = nombre;
                this.precio = precio;
                if (categoriaId > 0) {
                        Categoria cat = new Categoria();
                        cat.setId((int) categoriaId);
                        this.categoria = cat;
                }
        }

        // Constructor para nuevos productos
        public Producto(String nombre, double precio) {
                this.nombre = nombre;
                this.precio = precio;
        }
}