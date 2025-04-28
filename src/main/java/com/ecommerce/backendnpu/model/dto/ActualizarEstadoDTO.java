package com.ecommerce.backendnpu.model.dto;


    /**
     * DTO para actualizar el estado de un pedido
     */
    public class ActualizarEstadoDTO {
        private String nuevoEstado;
        private String comentario;

        // Getters y setters
        public String getNuevoEstado() {
            return nuevoEstado;
        }

        public void setNuevoEstado(String nuevoEstado) {
            this.nuevoEstado = nuevoEstado;
        }

        public String getComentario() {
            return comentario;
        }

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }

    }
