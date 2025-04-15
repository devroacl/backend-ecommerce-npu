package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.repository.ResenaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaServiceImpl implements ResenaService {
    private final ResenaRepository resenaRepository;

    public ResenaServiceImpl(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resena> obtenerTodasLasResenas() {
        return resenaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resena> obtenerResenaPorId(Long id) {
        return resenaRepository.findById(id);
    }

    @Override
    @Transactional
    public Resena guardarResena(Resena resena) {
        return resenaRepository.save(resena);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resena> obtenerResenasPorPuntaje(BigDecimal puntaje) {
        return resenaRepository.findByPuntaje(puntaje);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resena> obtenerResenasPorProducto(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resena> obtenerResenasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha final");
        }
        return resenaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional
    public void eliminarResena(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con id: " + id);
        }
        resenaRepository.deleteById(id);
    }
}