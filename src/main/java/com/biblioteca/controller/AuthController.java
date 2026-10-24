package com.biblioteca.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblioteca.seguridad.AutenticacionService;
import com.biblioteca.seguridad.Rol;
import com.biblioteca.seguridad.SesionUsuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AutenticacionService autenticacionService;

    public AuthController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    @GetMapping("/login")
    public String formulario(HttpSession sesion) {
        if (sesion.getAttribute(SesionUsuario.ATRIBUTO) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam(defaultValue = "ADMINISTRADOR") Rol rol,
            @RequestParam(defaultValue = "") String usuario,
            @RequestParam(defaultValue = "") String clave,
            HttpServletRequest request, Model model) {
        Optional<SesionUsuario> autenticado = autenticacionService.autenticar(rol, usuario, clave);
        if (autenticado.isEmpty()) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            model.addAttribute("rolSeleccionado", rol.name());
            return "login";
        }
        HttpSession sesion = request.getSession();
        request.changeSessionId();
        sesion.setAttribute(SesionUsuario.ATRIBUTO, autenticado.get());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String cerrarSesion(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/login";
    }
}
