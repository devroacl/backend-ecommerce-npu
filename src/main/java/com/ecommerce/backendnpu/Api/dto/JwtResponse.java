package com.ecommerce.backendnpu.Api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String correo;
    private String rol;

    public JwtResponse(String token, Long id, String correo, String rol) {
        this.token = token;
        this.id = id;
        this.correo = correo;
        this.rol = rol;
    }
}