package com.ecommerce.backendnpu.model;


/**
 * Enumerado que representa los posibles estados de un pedido
 */
public enum EstadoPedido {
    PENDIENTE,  // Recién creado, aún no procesado
    PROCESANDO, // En proceso de preparación
    ENVIADO,    // El pedido ha sido enviado
    ENTREGADO,  // El pedido fue entregado al cliente
    CANCELADO   // El pedido fue cancelado
}