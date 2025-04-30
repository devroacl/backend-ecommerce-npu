package com.ecommerce.backendnpu.security;


import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGenerator {

    public static void main(String[] args) {
        // Generar clave de 256 bits (32 bytes) para HS256
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        // Convertir a Base64
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("=== Clave JWT Segura (Base64) ===");
        System.out.println(base64Key); // Copia este valor a application.properties
    }
}