package com.biblioteca.seguridad;

import java.io.Serializable;

public class SesionUsuario implements Serializable {

    public static final String ATRIBUTO = "usuarioActual";

    private final String nombre;
    private final Rol rol;
    private final Long lectorId;

    public SesionUsuario(String nombre, Rol rol, Long lectorId) {
        this.nombre = nombre;
        this.rol = rol;
        this.lectorId = lectorId;
    }

    public String getNombre() {
        return nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public Long getLectorId() {
        return lectorId;
    }

    public String getRolEtiqueta() {
        return rol.getEtiqueta();
    }

    public boolean isAdministrador() {
        return rol == Rol.ADMINISTRADOR;
    }
}
