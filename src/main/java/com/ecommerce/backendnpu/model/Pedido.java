package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "estado_pedido", nullable = false)
    private EstadoPedido estadoPedido;

    // Constructor lógico para los tests
    public Pedido(Usuario usuario, EstadoPedido estadoPedido, BigDecimal total) {
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
        this.estadoPedido = estadoPedido;
        this.total = total;
    }
}