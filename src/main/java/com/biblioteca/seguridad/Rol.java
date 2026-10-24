package com.biblioteca.seguridad;

public enum Rol {
    ADMINISTRADOR("Administrador"),
    ALUMNO("Alumno");

    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
