package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.dto.AuthRequest;
import com.ecommerce.backendnpu.dto.AuthResponse;
import com.ecommerce.backendnpu.dto.RegisterRequest;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.UsuarioRepository;
import com.ecommerce.backendnpu.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre()); // Usando getNombre()
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setRut(request.getRut());
        usuario.setContrasena(request.getContrasena());

        Usuario savedUsuario = usuarioService.registrarNuevoUsuario(usuario, request.getRol());
        String jwtToken = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        savedUsuario.getCorreo(),
                        savedUsuario.getContrasena(),
                        java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        "ROLE_" + savedUsuario.getRol().getNombre()
                                )
                        )
                )
        );

        return AuthResponse.builder()
                .token(jwtToken)
                .correo(savedUsuario.getCorreo())
                .rol(savedUsuario.getRol().getNombre()) // Usando rol() en minúscula
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasena()
                )
        );

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        usuario.getCorreo(),
                        usuario.getContrasena(),
                        java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        "ROLE_" + usuario.getRol().getNombre()
                                )
                        )
                )
        );

        return AuthResponse.builder()
                .token(jwtToken)
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().getNombre()) // Usando rol() en minúscula
                .build();
    }
}