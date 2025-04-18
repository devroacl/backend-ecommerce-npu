package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.dto.AuthResponse;
import com.ecommerce.backendnpu.dto.LoginRequest;
import com.ecommerce.backendnpu.dto.RegisterRequest;
import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.security.JwtService;
import com.ecommerce.backendnpu.repository.RolRepository;
import com.ecommerce.backendnpu.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        // Validaciones básicas
        if (!StringUtils.hasText(request.getCorreo())) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (!StringUtils.hasText(request.getContrasena())) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (request.getContrasena().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        // Validar rol
        if (!Arrays.asList("ADMIN", "VENDEDOR", "COMPRADOR").contains(request.getRol().toUpperCase())) {
            throw new IllegalArgumentException("Rol no válido. Debe ser ADMIN, VENDEDOR o COMPRADOR");
        }

        // Verificar si el correo ya existe
        if (usuarioService.existeUsuarioPorCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Obtener el rol (con manejo adecuado del Optional)
        Rol rol = rolRepository.findByNombre(request.getRol().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Rol '%s' no encontrado en la base de datos", request.getRol())));

        // Crear y guardar el usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setRut(request.getRut());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(rol);
        usuario.setVerificar(false);

        usuarioService.saveUsuario(usuario);

        // Generar token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        String jwtToken = jwtService.generateToken(userDetails);

        // Crear respuesta
        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol().getNombre());

        return response;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getContrasena()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Correo o contraseña incorrectos");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
        String jwtToken = jwtService.generateToken(userDetails);

        Usuario usuario = usuarioService.findUsuarioByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol().getNombre());

        return response;
    }
}