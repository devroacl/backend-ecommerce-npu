package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 10)
    private String nombre; // Campo renombrado de "nombreUsuario" a "nombre"

    @Column(name = "apellido", nullable = false, length = 10)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 50)
    private String correo;

    @Column(name = "rut", nullable = false, length = 10)
    private String rut;

    @Column(name = "verificar", nullable = false)
    private boolean verificar;

    @Column(name = "token", length = 60)
    private String token;

    @Column(name = "contrasena", nullable = false, length = 15)
    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "rol_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_rol")
    )
    private Rol rol;
}