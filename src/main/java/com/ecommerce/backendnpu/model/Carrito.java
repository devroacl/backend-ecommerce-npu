package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    private LocalDateTime fechaCreacion;


    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    // Método para calcular el total
    public void calcularTotal() {
        this.total = BigDecimal.valueOf(items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getProducto().getPrecio())
                .sum());
    }
}