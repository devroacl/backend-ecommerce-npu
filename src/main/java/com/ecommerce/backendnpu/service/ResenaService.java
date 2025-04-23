package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResenaService {
    // Método para guardar reseña con validación de usuario y compra
    Resena guardarResena(Resena resena, Authentication authentication);

    // Método para actualizar reseña con validación
    Resena actualizarResena(Long id, Resena resena, Authentication authentication);

    // Métodos CRUD y consultas
    List<Resena> obtenerTodasLasResenas();
    Optional<Resena> obtenerResenaPorId(Long id);
    void eliminarResena(Long id, Authentication authentication);

    // Métodos específicos de consulta
    List<Resena> obtenerResenasPorPuntaje(BigDecimal puntaje);
    List<Resena> obtenerResenasPorProducto(Long productoId);
    List<Resena> obtenerResenasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);
}