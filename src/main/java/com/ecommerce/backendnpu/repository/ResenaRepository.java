package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Métodos CRUD básicos están incluidos en JpaRepository
    // Métodos personalizados (si los necesitas)
    List<Resena> findByUsuarioId(Integer usuarioId);

    List<Resena> findByProductosId(Integer productosId);

    // Puedes agregar más métodos de consulta personalizados si es necesario,
    // por ejemplo, para filtrar por puntaje, rango de fechas, etc.

}
