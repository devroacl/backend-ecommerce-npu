package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "comprador_id", nullable = false)
    private Usuario comprador;

    /***Para ***/
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> pedidoItems = new ArrayList<>();


    //Metodo para calcular el total de los productos del pedido
    public void calcularTotal() {
        this.total = BigDecimal.valueOf(pedidoItems.stream()
                .mapToDouble(item -> item.getCantidad() * item.getPreciounitario())
                .sum());
    }




    // Constructor lógico para los tests
    public Pedido(Usuario comprador, EstadoPedido estado, BigDecimal total) {
        this.fechaCreacion = LocalDateTime.now();
        this.comprador = comprador;
        this.estado = estado;
        this.total = total;
    }

    // Método helper para agregar items al pedido
    public void agregarItem(PedidoItem item) {
        pedidoItems.add(item);
        item.setPedido(this);
    }

    // Método helper para quitar items del pedido
    public void quitarItem(PedidoItem item) {
        pedidoItems.remove(item);
        item.setPedido(null);
    }
}