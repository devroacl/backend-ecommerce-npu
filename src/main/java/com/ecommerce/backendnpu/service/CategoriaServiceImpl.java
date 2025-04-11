package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.Categoria;
import com.ecommerce.backendnpu.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    private  CategoriaRepository categoriaRepository;

    //
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> findAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> findCategoriaByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteCategoriaById(Long id) {
        categoriaRepository.deleteById(id);
    }
}