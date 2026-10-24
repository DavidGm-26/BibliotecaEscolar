package com.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblioteca.service.AppException;
import com.biblioteca.service.SolicitudService;

@Controller
@RequestMapping("/gestion-solicitudes")
public class SolicitudAdminController {

    private final SolicitudService solicitudService;

    public SolicitudAdminController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("solicitudes", solicitudService.listarTodas());
        model.addAttribute("pendientes", solicitudService.contarPendientes());
        return "solicitudes/lista";
    }

    @PostMapping("/{id}/aprobar")
    public String aprobar(@PathVariable Long id, RedirectAttributes mensajes) {
        try {
            solicitudService.aprobar(id);
            mensajes.addFlashAttribute("mensaje", "Solicitud aprobada: el préstamo quedó registrado");
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gestion-solicitudes";
    }

    @PostMapping("/{id}/rechazar")
    public String rechazar(@PathVariable Long id, @RequestParam(defaultValue = "") String motivo,
            RedirectAttributes mensajes) {
        try {
            solicitudService.rechazar(id, motivo);
            mensajes.addFlashAttribute("mensaje", "Solicitud rechazada");
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gestion-solicitudes";
    }
}
