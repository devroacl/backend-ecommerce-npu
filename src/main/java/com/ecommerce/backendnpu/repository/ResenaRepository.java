package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    // Método para encontrar reseñas por puntaje
    List<Resena> findByPuntaje(int puntaje);

    // Método para encontrar reseñas por producto (suponiendo que Resena tiene una relación con Producto)
    List<Resena> findByProductoId(Long productoId);

    // Método para obtener reseñas por rango de fechas
    List<Resena> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}


