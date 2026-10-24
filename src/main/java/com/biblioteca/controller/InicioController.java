package com.biblioteca.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.biblioteca.model.Prestamo;
import com.biblioteca.seguridad.SesionUsuario;
import com.biblioteca.service.LectorService;
import com.biblioteca.service.LibroService;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.service.SolicitudService;

@Controller
public class InicioController {

    private static final int ULTIMOS_PRESTAMOS = 5;

    private final LibroService libroService;
    private final LectorService lectorService;
    private final PrestamoService prestamoService;
    private final SolicitudService solicitudService;

    public InicioController(LibroService libroService, LectorService lectorService,
            PrestamoService prestamoService, SolicitudService solicitudService) {
        this.libroService = libroService;
        this.lectorService = lectorService;
        this.prestamoService = prestamoService;
        this.solicitudService = solicitudService;
    }

    @GetMapping("/")
    public String inicio(@SessionAttribute(SesionUsuario.ATRIBUTO) SesionUsuario usuario, Model model) {
        if (!usuario.isAdministrador()) {
            return "redirect:/catalogo";
        }
        List<Prestamo> prestamos = prestamoService.listar();
        model.addAttribute("totalLibros", libroService.listar().size());
        model.addAttribute("totalLectores", lectorService.listar().size());
        model.addAttribute("pendientes", prestamos.stream().filter(p -> p.getFechaDevolucion() == null).count());
        model.addAttribute("vencidos", prestamos.stream().filter(Prestamo::isVencido).count());
        model.addAttribute("solicitudesPendientes", solicitudService.contarPendientes());
        model.addAttribute("ultimosPrestamos", prestamos.stream().limit(ULTIMOS_PRESTAMOS).toList());
        return "inicio";
    }
}
