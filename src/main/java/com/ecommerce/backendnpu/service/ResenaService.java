package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResenaService {
    List<Resena> obtenerTodasLasResenas();
    Optional<Resena> obtenerResenaPorId(Long id);
    Resena guardarResena(Resena resena);
    List<Resena> obtenerResenasPorPuntaje(BigDecimal puntaje); // Cambiado a BigDecimal
    List<Resena> obtenerResenasPorProducto(Long productoId);
    List<Resena> obtenerResenasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);
    void eliminarResena(Long id);
}