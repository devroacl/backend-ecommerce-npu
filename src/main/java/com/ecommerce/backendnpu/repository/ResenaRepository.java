package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Métodos básicos de consulta
    List<Resena> findByPuntaje(BigDecimal puntaje);

    List<Resena> findByProductoId(Long productoId);

    List<Resena> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Métodos adicionales para mejorar la funcionalidad

    // Método para verificar si ya existe una reseña
    boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    // Encontrar reseñas por usuario
    List<Resena> findByUsuarioId(Long usuarioId);

    // Buscar reseñas con puntaje mayor o igual a un valor
    List<Resena> findByPuntajeGreaterThanEqual(BigDecimal puntajeMinimo);

    // Buscar reseñas con puntaje menor o igual a un valor
    List<Resena> findByPuntajeLessThanEqual(BigDecimal puntajeMaximo);

    // Buscar reseñas por producto ordenadas por fecha (más recientes primero)
    List<Resena> findByProductoIdOrderByFechaDesc(Long productoId);

    // Calcular el promedio de puntajes para un producto específico
    @Query("SELECT AVG(r.puntaje) FROM Resena r WHERE r.producto.id = :productoId")
    BigDecimal calcularPromedioPuntajePorProducto(@Param("productoId") Long productoId);

    // Contar el número de reseñas por producto
    @Query("SELECT COUNT(r) FROM Resena r WHERE r.producto.id = :productoId")
    Long contarResenasPorProducto(@Param("productoId") Long productoId);

    // Buscar reseñas que contengan texto específico en la descripción (útil para búsquedas)
    List<Resena> findByDescripcionContainingIgnoreCase(String textoDescripcion);
}