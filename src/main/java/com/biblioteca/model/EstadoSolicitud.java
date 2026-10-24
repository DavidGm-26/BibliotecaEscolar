package com.biblioteca.model;

public enum EstadoSolicitud {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada");

    private final String etiqueta;

    EstadoSolicitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
