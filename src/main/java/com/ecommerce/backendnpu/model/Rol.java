package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 20)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    // Constantes para los roles predefinidos
    public static final Long ID_COMPRADOR = 1L;
    public static final Long ID_VENDEDOR = 2L;
}