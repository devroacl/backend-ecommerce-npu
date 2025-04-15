package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.repository.ResenaRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaServiceImpl implements ResenaService {
    private final ResenaRepository resenaRepository;

    @Autowired
    public ResenaServiceImpl(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    public List<Resena> obtenerTodasLasResenas() {
        return resenaRepository.findAll();
    }

    @Override
    public Optional<Resena> obtenerResenaPorId(Long id) {
        return resenaRepository.findById(id);
    }

    @Override
    public Resena guardarResena(Resena resena) {
        return resenaRepository.save(resena);
    }

    @Override
    public List<Resena> obtenerResenasPorPuntaje(int puntaje) {
        return resenaRepository.findByPuntaje(puntaje);
    }

    @Override
    public List<Resena> obtenerResenasPorProducto(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    @Override
    public List<Resena> obtenerResenasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return resenaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
}
