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
import org.springframework.http.HttpStatus;
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

import java.util.Map;

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
        // Validar correo único
        if (usuarioRepository.existsByCorreo(request.correo())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo ya está registrado"));
        }

        // ======= Validación RUT chileno (módulo 11) =======
        String rut = request.rut()
                .replace(".", "")
                .replace("-", "")
                .trim()
                .toUpperCase();

        // Validación básica de formato
        if (rut.length() < 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "RUT incompleto"));
        }

        // Separar número y dígito verificador
        String rutNumeroStr = rut.substring(0, rut.length() - 1);
        char dvIngresado = rut.charAt(rut.length() - 1);

        // Validar que el número sea válido
        if (!rutNumeroStr.matches("\\d+")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Parte numérica del RUT inválida"));
        }

        try {
            int rutNumero = Integer.parseInt(rutNumeroStr);
            char dvCalculado = calcularDigitoVerificador(rutNumero);

            // Validar dígito verificador
            if (dvIngresado != dvCalculado) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "RUT inválido",
                        "detalle", "Dígito verificador incorrecto. Debería ser: " + dvCalculado
                ));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "RUT numérico inválido"));
        }
        // ======= Fin validación RUT =======

        // Resto de la lógica de registro...
        Rol rolUsuario;
        try {
            switch (request.tipoUsuario().toUpperCase()) {
                case "COMPRADOR":
                    rolUsuario = rolRepository.findByNombre(ERol.ROLE_COMPRADOR)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                    break;
                case "VENDEDOR":
                    rolUsuario = rolRepository.findByNombre(ERol.ROLE_VENDEDOR)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                    break;
                default:
                    return ResponseEntity.badRequest().body(Map.of("error", "Tipo de usuario inválido"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error de configuración: " + e.getMessage()));
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.correo());
        usuario.setContrasena(passwordEncoder.encode(request.contrasena()));
        usuario.setNombreUsuario(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setRut(formatearRut(rut)); // Guardar RUT formateado
        usuario.setRol(rolUsuario);

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "detalles", Map.of(
                        "id", usuario.getId(),
                        "correo", usuario.getCorreo(),
                        "rut", usuario.getRut(),
                        "rol", usuario.getRol().getNombre().name()
                )
        ));
    }

    // Método para calcular dígito verificador
    private char calcularDigitoVerificador(int rut) {
        int m = 0;
        int s = 1;

        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }

        return (char) (s != 0 ? s + 47 : 75); // 75 = 'K', 47 = '0' - 1 en ASCII
    }

    // Método para formatear RUT (opcional)
    private String formatearRut(String rut) {
        StringBuilder resultado = new StringBuilder(rut.replaceAll("[^\\dK]", ""));
        int length = resultado.length() - 1;

        for (int i = length - 1; i > 0; i -= 3) {
            resultado.insert(i, ".");
        }

        return resultado.insert(resultado.length() - 1, "-").toString();
    }


}