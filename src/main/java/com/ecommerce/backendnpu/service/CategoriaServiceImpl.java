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

    @Override
    public List<Categoria> findAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public Optional<Categoria> findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void deleteCategoriaById(Long id) {
        categoriaRepository.deleteById(id);
    }
}