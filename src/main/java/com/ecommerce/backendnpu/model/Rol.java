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

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false,length = 20)
    private ERol nombre; // ADMIN, VENDEDOR, COMPRADOR

    // Getters y Setters
}

