package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResenaService {
    List<Resena> obtenerTodasLasResenas();
    Optional<Resena> obtenerResenaPorId(Long id);
    Resena guardarResena(Resena resena);
    List<Resena> obtenerResenasPorPuntaje(int puntaje);
    List<Resena> obtenerResenasPorProducto(Long productoId);
    List<Resena> obtenerResenasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

}
