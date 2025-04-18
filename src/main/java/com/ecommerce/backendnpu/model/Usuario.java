package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name ="usuario")
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con argumentos para todos los campos
public class Usuario {
    //Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre", nullable = false,length =10)
    private  String nombreUsuario;

    @Column(name = "apellido", nullable = false,length =10)
    private  String apellido;

    @Column(name = "correo",nullable = false,length = 50)
    private String correo;

    @Column(name = "rut",nullable = false,length = 10)
    private String rut;

    @Column(name = "verificar",nullable = false)
    private boolean verificar;

    @Column(name = "token",length = 60,nullable = true)
    private String token;

    @Column(name ="contrasena",nullable = false,length =15 )
    private String contrasena;

    /**@ManyToOne  ESTO CAUSABA ERROR CON LA LLAVE FORANEA
    @JoinColumn(name = "rol_id",nullable = false)
    private Rol rolId; **/

    // Renombramos el atributo a 'rol' y forzamos el nombre exacto de la columna FK:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "rol_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_rol")
    )
    private Rol rol;
}