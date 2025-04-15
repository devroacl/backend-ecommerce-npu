package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
        List<Categoria> findAllCategorias();
        Optional<Categoria> findCategoriaById(Long id);
        Optional<Categoria> findCategoriaByNombre(String nombre);
        Categoria saveCategoria(Categoria categoria);
        void deleteCategoriaById(Long id);
    }

