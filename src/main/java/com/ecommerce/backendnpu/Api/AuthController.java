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
            default:
                return ResponseEntity.badRequest().body("Error: Tipo de usuario inválido");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.correo());
        usuario.setContrasena(passwordEncoder.encode(request.contrasena()));
        usuario.setNombreUsuario(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setRut(request.rut());
        usuario.setRol(rolUsuario);
        //usuario.setActivo(true);

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

    public boolean validarRut(String rut) {
        // Si viene vacío retornamos false
        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        // Eliminamos puntos y guiones
        rut = rut.replace(".", "").replace("-", "").trim().toUpperCase();

        // Verificamos que tenga largo mínimo
        if (rut.length() < 2) {
            return false;
        }

        // Separamos el número del dígito verificador
        String rutNumero = rut.substring(0, rut.length() - 1);
        char dvIngresado = rut.charAt(rut.length() - 1);

        try {
            // Convertimos a entero
            int rutInt = Integer.parseInt(rutNumero);

            // Calculamos el dígito verificador esperado
            char dvEsperado = calcularDv(rutInt);

            // Comparamos el dígito verificador ingresado con el calculado
            return dvIngresado == dvEsperado;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Calcula el dígito verificador de un RUT usando el algoritmo del módulo 11.
     * @param rut Número del RUT sin dígito verificador
     * @return Carácter correspondiente al dígito verificador ('0'-'9' o 'K')
     */
    public char calcularDv(int rut) {
        int m = 0;
        int s = 1;

        // Algoritmo módulo 11
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }

        // Determinamos el dígito verificador
        return (char) (s != 0 ? s + '0' - 1 : 'K');
    }

    /**
     * Método auxiliar para formatear un RUT con puntos y guión.
     * @param rut El RUT sin formato (solo números y dígito verificador)
     * @return RUT formateado (ejemplo: 12.345.678-9)
     */
    public String formatearRut(String rut) {
        // Eliminamos puntos y guiones si los tiene
        rut = rut.replace(".", "").replace("-", "").trim();

        // Separamos número y dígito verificador
        String numero = rut.substring(0, rut.length() - 1);
        String dv = rut.substring(rut.length() - 1);

        // Formateamos con puntos
        StringBuilder resultado = new StringBuilder();
        int count = 0;

        for (int i = numero.length() - 1; i >= 0; i--) {
            if (count == 3 && i != 0) {
                resultado.insert(0, ".");
                count = 0;
            }
            resultado.insert(0, numero.charAt(i));
            count++;
        }

        // Agregamos el guión y dígito verificador
        return resultado + "-" + dv;
    }

}