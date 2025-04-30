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
@Table(name ="usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    //Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 50)
    private String correo;

    @Column(name = "rut", nullable = false,length = 9)
    private String rut;


    @Column(name ="contrasena", nullable = false)
    private String contrasena;

    //private boolean activo = true;

    // Un usuario tiene un solo rol (relación muchos a uno)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // Método para obtener autoridades (para Spring Security)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.getNombre().name()));
    }

    // Método de conveniencia para verificar el rol
    public boolean tieneRol(ERol rolEnum) {
        return rol != null && rol.getNombre() == rolEnum;
    }
}