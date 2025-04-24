package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 50)
    private String correo;

    @Column(name = "rut", nullable = false, length = 10)
    private String rut;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol == null || rol.getNombre() == null) {
            return List.of(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        }
        return List.of(new SimpleGrantedAuthority(rol.getNombre().name()));
    }

    public boolean tieneRol(ERol rolEnum) {
        return rol != null && rol.getNombre() == rolEnum;
    }
}