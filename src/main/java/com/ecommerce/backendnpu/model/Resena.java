package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import jdk.jfr.Description;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

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

    @Column(name= "fecha", nullable = false)
    private DateTimeException fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        @ManyToOne
        @JoinColumn(name = "producto_id", nullable = false) // Clave foránea
        private Producto producto;


        public Producto getProducto() {
            return producto;
        }

        public void setProducto(Producto producto) {
            this.producto = producto;

        }
        }





