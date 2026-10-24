package com.biblioteca.seguridad;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class DatosGlobales {

    @ModelAttribute(SesionUsuario.ATRIBUTO)
    public SesionUsuario usuarioActual(HttpSession sesion) {
        return (SesionUsuario) sesion.getAttribute(SesionUsuario.ATRIBUTO);
    }
}
