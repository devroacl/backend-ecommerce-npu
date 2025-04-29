package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String nombre;

        @Column(length = 1000)
        private String descripcion;

        @Column(nullable = false)
        private Double precio;

        private String imagen;

        private Integer stock;

        @Column(name = "fecha_creacion")
        private LocalDateTime fechaCreacion;

        @Column(name = "fecha_actualizacion")
        private LocalDateTime fechaActualizacion;


        @Column(name = "activo", nullable = false)
        private Boolean activo = true;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "categoria_id")
        private Categoria categoria;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "vendedor_id")
        private Usuario vendedor;


        // Métodos de ciclo de vida
        @PrePersist
        protected void onCreate() {
                fechaCreacion = LocalDateTime.now();
                fechaActualizacion = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
                fechaActualizacion = LocalDateTime.now();
        }


}