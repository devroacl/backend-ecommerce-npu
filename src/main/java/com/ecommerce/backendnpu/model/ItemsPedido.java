package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "preciounitario")
    private Integer preciounitario;

    //pedidos_id INT (Conecta con entidad pedidos)

    @ManyToOne
    @JoinColumn(name = "pedidosId",nullable = false)
    private Pedido pedidoId;


    /***productos_id Int (conecta con la entidad productos)
    Falta conectar con Entidad producto***/

    @ManyToOne
    @JoinColumn(name = "pedido",nullable = false)
    private Producto producto;


}
