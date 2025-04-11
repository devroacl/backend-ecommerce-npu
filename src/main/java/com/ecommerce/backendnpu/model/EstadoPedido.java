package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "estadoPedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombreestado",nullable = false,length =45,unique = true)
    private String nombreEstado;

}
