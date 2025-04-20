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

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_modificacion")
    private LocalDateTime ultimaModificacion;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    // Método para actualizar el total del carrito
    public void actualizarTotal() {
        this.total = items.stream()
                .map(item -> BigDecimal.valueOf(item.getCantidad())
                        .multiply(BigDecimal.valueOf(item.getProducto().getPrecio())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Método para agregar un item al carrito
    public void agregarItem(ItemCarrito item) {
        items.add(item);
        item.setCarrito(this);
        actualizarTotal();
    }

    // Método para eliminar un item del carrito
    public void eliminarItem(ItemCarrito item) {
        items.remove(item);
        item.setCarrito(null);
        actualizarTotal();
    }

    // Método para convertir el carrito en un pedido
    public Pedido convertirAPedido(EstadoPedido estadoInicial) {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setUsuario(this.usuario);
        pedido.setEstadoPedido(estadoInicial);

        return pedido;
    }
}