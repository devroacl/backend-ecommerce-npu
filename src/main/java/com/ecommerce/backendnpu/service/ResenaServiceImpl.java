package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.model.ERol;
import com.ecommerce.backendnpu.model.Resena;
import com.ecommerce.backendnpu.model.Usuario;
import com.ecommerce.backendnpu.repository.ResenaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaServiceImpl implements ResenaService {

    private final ResenaRepository resenaRepository;
    private final PedidoService pedidoService;

    // Constructor con ambas dependencias
    public ResenaServiceImpl(
            ResenaRepository resenaRepository,
            PedidoService pedidoService
    ) {
        this.resenaRepository = resenaRepository;
        this.pedidoService = pedidoService;
    }


    @Override
    @Transactional
    public Resena guardarResena(Resena resena, Authentication authentication) {
        // Validar usuario y sus permisos
        Usuario usuario = validarUsuarioComprador(authentication);

        // Validar compra previa
       // if (!pedidoService.haCompradoProducto(usuario.getId(), resena.getProducto().getId())) {
            throw new IllegalArgumentException("Solo puedes reseñar productos que hayas comprado");
        //}

        // Verificar si el usuario ya ha dejado una reseña para este producto
        //if (resenaRepository.existsByUsuarioIdAndProductoId(usuario.getId(), resena.getProducto().getId())) {
        //    throw new IllegalArgumentException("Ya has dejado una reseña para este producto");
        //}

        // Establecer el usuario y fecha actual si es nueva reseña
       // resena.setUsuario(usuario);
       // if (resena.getFecha() == null) {
          //  resena.setFecha(LocalDate.now());
        //}

        //return resenaRepository.save(resena);
    }

    @Override
    @Transactional
    public Resena actualizarResena(Long id, Resena resena, Authentication authentication) {
        // Validar usuario y sus permisos
        Usuario usuario = validarUsuarioComprador(authentication);

        // Obtener la reseña existente
        Resena resenaExistente = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));

        // Verificar si la reseña pertenece al usuario actual
        if (resenaExistente.getUsuario().getId() != usuario.getId()) {
            throw new IllegalArgumentException("Solo puedes modificar tus propias reseñas");
        }

        // Actualizar campos permitidos pero mantener usuario y producto originales
        resenaExistente.setPuntaje(resena.getPuntaje());
        resenaExistente.setDescripcion(resena.getDescripcion());
        // No permitir cambiar el producto reseñado

        return resenaRepository.save(resenaExistente);
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
    public void eliminarResena(Long id, Authentication authentication) {
        // Validar usuario y sus permisos
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));

        // Solo el dueño de la reseña o un administrador puede eliminarla
        if (resena.getUsuario().getId() != usuario.getId() &&
                usuario.getRol().getNombre() != ERol.ROLE_ADMIN) {
            throw new IllegalArgumentException("No tienes permisos para eliminar esta reseña");
        }

        resenaRepository.deleteById(id);
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

    // Método auxiliar para validar usuario comprador
    private Usuario validarUsuarioComprador(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Debes iniciar sesión para realizar esta acción");
        }

        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Validar rol COMPRADOR
        if (usuario.getRol().getNombre() != ERol.ROLE_COMPRADOR) {
            throw new IllegalArgumentException("Solo los compradores pueden crear reseñas");
        }

        return usuario;
    }
}