package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Cambiado de int a BigDecimal para que coincida con la entidad
    List<Resena> findByPuntaje(BigDecimal puntaje);

    List<Resena> findByProductoId(Long productoId);

    List<Resena> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}