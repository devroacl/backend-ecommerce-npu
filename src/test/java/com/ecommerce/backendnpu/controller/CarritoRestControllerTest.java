package com.ecommerce.backendnpu.controller;

import com.ecommerce.backendnpu.Api.CarritoRestController;
import com.ecommerce.backendnpu.model.*;
import com.ecommerce.backendnpu.service.CarritoService;
import com.ecommerce.backendnpu.service.PedidoService;
import com.ecommerce.backendnpu.service.ProductoService;
import com.ecommerce.backendnpu.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoRestControllerTest {

    @Mock
    private CarritoService carritoService;

    @Mock
    private ProductoService productoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CarritoRestController carritoRestController;

    private Usuario usuarioComprador;
    private Carrito carrito;
    private Producto producto;
    private CarritoItem carritoItem;

    @BeforeEach
    void setUp() {
        // Configurar contexto de seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("comprador@test.com");

        // Configurar usuario autenticado
        usuarioComprador = new Usuario();
        usuarioComprador.setId(1L);
        usuarioComprador.setCorreo("comprador@test.com");
        when(usuarioService.findByCorreo("comprador@test.com")).thenReturn(Optional.of(usuarioComprador));

        // Configurar producto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(1200.0);
        producto.setStock(10);

        // Configurar ítem del carrito
        carritoItem = new CarritoItem();
        carritoItem.setId(1L);
        carritoItem.setProducto(producto);
        carritoItem.setCantidad(2);

        // Configurar carrito
        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setUsuario(usuarioComprador);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setTotal(BigDecimal.valueOf(2400.0));
        carrito.getItems().add(carritoItem);
    }

    @Test
    void confirmarPedido_CarritoValido_DeberiaRetornarPedido() {
        // Configurar mocks
        when(carritoService.findByUsuario(usuarioComprador)).thenReturn(Optional.of(carrito));
        when(pedidoService.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar método
        ResponseEntity<?> response = carritoRestController.confirmarPedido();

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Pedido);
        verify(productoService, times(1)).save(producto); // Stock actualizado
        verify(carritoService, times(1)).vaciarCarrito(usuarioComprador);
    }

    @Test
    void confirmarPedido_CarritoVacio_DeberiaRetornarError() {
        // Carrito vacío
        carrito.getItems().clear();
        when(carritoService.findByUsuario(usuarioComprador)).thenReturn(Optional.of(carrito));

        // Ejecutar método
        ResponseEntity<?> response = carritoRestController.confirmarPedido();

        // Verificar resultados
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Carrito vacío", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void confirmarPedido_StockInsuficiente_DeberiaRetornarError() {
        // Producto sin stock suficiente
        producto.setStock(1);
        when(carritoService.findByUsuario(usuarioComprador)).thenReturn(Optional.of(carrito));

        // Ejecutar método
        ResponseEntity<?> response = carritoRestController.confirmarPedido();

        // Verificar resultados
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((String) ((Map<?, ?>) response.getBody()).get("error")).contains("Stock insuficiente"));
    }

    @Test
    void confirmarPedido_UsuarioNoAutenticado_DeberiaLanzarExcepcion() {
        // Simular usuario no autenticado
        when(usuarioService.findByCorreo("comprador@test.com")).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        assertThrows(RuntimeException.class, () -> carritoRestController.confirmarPedido());
    }
}