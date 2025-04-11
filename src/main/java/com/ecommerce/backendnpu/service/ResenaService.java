package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;

import java.util.List;
import java.util.Optional;

public interface ResenaService {
    List<Resena> getAllResenas();

    Optional<Resena> getResenaById(Integer id);

    Resena saveResena(Resena resena);

    Resena updateResena(Resena resena);

    void deleteResena(Integer id);

    List<Resena> getResenasByUsuarioId(Integer usuarioId);

    List<Resena> getResenasByProductosId(Integer productosId);

    // Puedes agregar más métodos específicos de la lógica de negocio,
    // por ejemplo, calcular el puntaje promedio de un producto.
}
