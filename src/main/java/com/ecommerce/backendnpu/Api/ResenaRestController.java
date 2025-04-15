package com.ecommerce.backendnpu.Api;

import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.service.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Resena> crearResena(@RequestBody Resena resena) {
        Resena nuevaResena = resenaService.guardarResena(resena);
        return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
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
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        if (resenaService.obtenerResenaPorId(id).isPresent()) {
            resenaService.eliminarResena(id);  // Usamos el servicio en lugar del repository
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Actualizar una reseña
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(@PathVariable Long id, @RequestBody Resena resena) {
        return resenaService.obtenerResenaPorId(id)
                .map(resenaExistente -> {
                    resena.setId(id); // Asegurarnos que el ID coincida
                    Resena resenaActualizada = resenaService.guardarResena(resena);
                    return new ResponseEntity<>(resenaActualizada, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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