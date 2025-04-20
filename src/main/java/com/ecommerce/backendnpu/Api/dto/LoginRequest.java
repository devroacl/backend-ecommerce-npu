package com.ecommerce.backendnpu.Api.dto;

import jakarta.validation.constraints.NotBlank;

// Archivo: controller/dto/LoginRequest.java
public record LoginRequest(
        @NotBlank String correo,
        @NotBlank String contrasena
) {}
