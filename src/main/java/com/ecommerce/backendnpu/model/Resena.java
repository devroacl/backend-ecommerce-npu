package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.math.BigDecimal;
import java.time.DateTimeException;

@Entity
@Table(name= "resena")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false) // Clave foránea
    private Producto producto;

    }








