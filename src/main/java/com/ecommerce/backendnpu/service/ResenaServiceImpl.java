package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.repository.ResenaRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaServiceImpl implements ResenaService {
    private final ResenaRepository resenaRepository;

    // Inyección por constructor
    public ResenaServiceImpl(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }

    @Override
    public Optional<Resena> getResenaById(Integer id) {
        return Optional.empty();
    }


    @Override
    public Resena saveResena(Resena resena) {
        return resenaRepository.save(resena);
    }

    @Override
    public Resena updateResena(Resena resena) {
        //  Aquí podrías agregar lógica de validación o negocio adicional antes de guardar
        return resenaRepository.save(resena);
    }

    @Override
    public void deleteResena(Integer id) {

    }


    @Override
    public List<Resena> getResenasByUsuarioId(Integer usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Resena> getResenasByProductosId(Integer productosId) {
        return resenaRepository.findByProductosId(productosId);
    }

    //  Lógica de negocio adicional (ejemplo)
    public double calcularPuntajePromedioProducto(Integer productoId) {
        List<Resena> resenas = resenaRepository.findByProductosId(productoId);
        if (resenas.isEmpty()) {
            return 0.0; // O lanzar una excepción, dependiendo de tus requerimientos
        }
        double suma = 0;
        for (Resena resena : resenas) {
            suma += resena.getPuntaje().doubleValue(); // Convertir a double para la suma
        }
        return suma / resenas.size();
    }

    //  Más lógica de negocio si es necesario
}
