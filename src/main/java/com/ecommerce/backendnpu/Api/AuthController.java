package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.Api.dto.JwtResponse;
import com.ecommerce.backendnpu.Api.dto.LoginRequest;
import com.ecommerce.backendnpu.Api.dto.RegistroRequest;
import com.ecommerce.backendnpu.model.ERol;
import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.RolRepository;
import com.ecommerce.backendnpu.repository.UsuarioRepository;
import com.ecommerce.backendnpu.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          RolRepository rolRepository,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.correo(),
                        request.contrasena()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        Usuario usuario = usuarioRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado"));

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().getNombre().name()
        ));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            return ResponseEntity.badRequest().body("Error: Correo ya registrado");
        }

        if (!validarRut(request.rut())) {
            return ResponseEntity.badRequest().body("Error: RUT inválido");
        }

        Rol rolUsuario;
        switch (request.tipoUsuario().toUpperCase()) {
            case "COMPRADOR":
                rolUsuario = rolRepository.findByNombre(ERol.ROLE_COMPRADOR)
                        .orElseThrow(() -> new RuntimeException("Error: Rol COMPRADOR no configurado"));
                break;
            case "VENDEDOR":
                rolUsuario = rolRepository.findByNombre(ERol.ROLE_VENDEDOR)
                        .orElseThrow(() -> new RuntimeException("Error: Rol VENDEDOR no configurado"));
                break;
            case "AMBOS":
                rolUsuario = rolRepository.findByNombre(ERol.ROLE_VENDEDOR)
                        .orElseThrow(() -> new RuntimeException("Error: Rol VENDEDOR no configurado"));
                break;
            default:
                return ResponseEntity.badRequest().body("Error: Tipo de usuario inválido");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.correo());
        usuario.setContrasena(passwordEncoder.encode(request.contrasena()));
        usuario.setNombreUsuario(request.nombreUsuario());
        usuario.setApellido(request.apellido());
        usuario.setRut(request.rut());
        usuario.setVerificar(false);
        usuario.setRol(rolUsuario);
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        // Autenticar al usuario recién registrado
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.correo(),
                        request.contrasena()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().getNombre().name()
        ));
    }

    // Métodos de validación de RUT sin cambios...
    private boolean validarRut(String rut) {
        rut = rut.replace(".", "").replace("-", "");
        if (rut.length() < 2) return false;

        String numero = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        try {
            int numeroInt = Integer.parseInt(numero);
            return calcularDv(numeroInt) == Character.toUpperCase(dv);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private char calcularDv(int rut) {
        int suma = 0;
        int multiplicador = 2;
        while (rut > 0) {
            int digito = rut % 10;
            suma += digito * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
            rut /= 10;
        }
        int resultado = 11 - (suma % 11);
        return switch (resultado) {
            case 11 -> '0';
            case 10 -> 'K';
            default -> (char) (resultado + '0');
        };
    }
}