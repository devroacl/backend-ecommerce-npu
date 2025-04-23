package com.ecommerce.backendnpu.Api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Archivo: controller/dto/RegistroRequest.java
public record RegistroRequest(
        @NotBlank String correo,
        @NotBlank @Size(min = 6, max = 15) String contrasena,
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank String rut,
        @NotBlank String tipoUsuario  // COMPRADOR, VENDEDOR, o AMBOS
) {}