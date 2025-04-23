package com.ecommerce.backendnpu.Api;


import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.service.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resenas")
public class ResenaRestController {
    private final ResenaService resenaService;

    // Crear una nueva reseña
    @PostMapping
    public ResponseEntity<?> crearResena(@RequestBody Resena resena, Authentication authentication) {
        try {
            Resena nuevaResena = resenaService.guardarResena(resena, authentication);
            return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todas las reseñas
    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodasLasResenas() {
        List<Resena> resenas = resenaService.obtenerTodasLasResenas();
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    // Obtener una reseña por ID
    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerResenaPorId(@PathVariable Long id) {
        Optional<Resena> resena = resenaService.obtenerResenaPorId(id);
        return resena.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Eliminar una reseña por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResena(@PathVariable Long id, Authentication authentication) {
        try {
            resenaService.eliminarResena(id, authentication);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una reseña
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResena(@PathVariable Long id, @RequestBody Resena resena, Authentication authentication) {
        try {
            Resena resenaActualizada = resenaService.actualizarResena(id, resena, authentication);
            return new ResponseEntity<>(resenaActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Endpoints adicionales específicos para reseñas
    @GetMapping("/por-puntaje/{puntaje}")
    public ResponseEntity<List<Resena>> obtenerResenasPorPuntaje(@PathVariable BigDecimal puntaje) {
        List<Resena> resenas = resenaService.obtenerResenasPorPuntaje(puntaje);
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @GetMapping("/por-producto/{productoId}")
    public ResponseEntity<List<Resena>> obtenerResenasPorProducto(@PathVariable Long productoId) {
        List<Resena> resenas = resenaService.obtenerResenasPorProducto(productoId);
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @GetMapping("/por-fecha")
    public ResponseEntity<List<Resena>> obtenerResenasPorFecha(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {
        List<Resena> resenas = resenaService.obtenerResenasPorFecha(fechaInicio, fechaFin);
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }
}