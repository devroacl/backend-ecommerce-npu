package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
        List<Categoria> findAllCategorias();
        Optional<Categoria> findById(Long id);
        Optional<Categoria> findByNombre(String nombre);
        Categoria saveCategoria(Categoria categoria);
        void deleteCategoriaById(Long id);
    }

