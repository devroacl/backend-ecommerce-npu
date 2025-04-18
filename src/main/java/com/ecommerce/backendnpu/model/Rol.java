// Rol.java
package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
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

    // Constantes para poblar o referenciar directamente
    public static final Long ID_ADMIN    = 1L;
    public static final Long ID_VENDEDOR = 2L;
    public static final Long ID_COMPRADOR= 3L;
}