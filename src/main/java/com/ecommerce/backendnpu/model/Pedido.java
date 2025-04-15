package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "fecha",nullable = false)
    public LocalDateTime fecha;

    //usuario-id

    @ManyToOne
    @JoinColumn(name = "usuarioId",nullable = false)
    private Usuario usuarioId;

    //Estadopedido (conectado con estado pedido)

    @ManyToOne
    @JoinColumn(name = "estadopedido",nullable = false)
    private EstadoPedido estadopedido;


    //Setter y getter porque data de lombok no los construye. Debo arreglar eso. 


    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setEstadopedido(EstadoPedido estadopedido) {
        this.estadopedido = estadopedido;
    }

    public void setUsuario(Usuario usuario) {
    }

    public void setFechaPedido(LocalDateTime now) {
    }

    public void setTotal(BigDecimal zero) {
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
    }

    //Getter 
    
}
