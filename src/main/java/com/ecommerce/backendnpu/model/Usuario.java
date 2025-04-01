package com.ecommerce.backendnpu.model;

import jakarta.persistence.*;

@Entity
@Table(name ="usuario")
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

    @ManyToOne
    @JoinColumn(name = "rolId",nullable = false)
    private Rol rolId;











}
