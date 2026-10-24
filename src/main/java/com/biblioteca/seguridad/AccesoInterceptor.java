package com.biblioteca.seguridad;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AccesoInterceptor implements HandlerInterceptor {

    private static final Set<String> RUTAS_ALUMNO = Set.of("/", "/catalogo", "/solicitudes", "/mis-solicitudes",
            "/mis-prestamos", "/logout");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String contexto = request.getContextPath();
        HttpSession sesion = request.getSession(false);
        SesionUsuario usuario = sesion == null ? null : (SesionUsuario) sesion.getAttribute(SesionUsuario.ATRIBUTO);

        if (usuario == null) {
            response.sendRedirect(contexto + "/login");
            return false;
        }
        String ruta = request.getRequestURI().substring(contexto.length());
        if (usuario.isAdministrador() || RUTAS_ALUMNO.contains(ruta)) {
            return true;
        }
        response.sendRedirect(contexto + "/catalogo");
        return false;
    }
}
