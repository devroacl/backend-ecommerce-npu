package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.Rol;
import com.ecommerce.backendnpu.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RolRestController {
    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> obtenerTodosLosRoles() {
        return ResponseEntity.ok(rolService.obtenerTodosLosRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(@PathVariable Long id) {
        return rolService.obtenerRolPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rolService.guardarRol(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Long id, @RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.actualizarRol(id, rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints específicos para roles predefinidos
    @GetMapping("/comprador")
    public ResponseEntity<Rol> obtenerRolComprador() {
        return ResponseEntity.ok(rolService.obtenerRolComprador());
    }

    @GetMapping("/vendedor")
    public ResponseEntity<Rol> obtenerRolVendedor() {
        return ResponseEntity.ok(rolService.obtenerRolVendedor());
    }
}