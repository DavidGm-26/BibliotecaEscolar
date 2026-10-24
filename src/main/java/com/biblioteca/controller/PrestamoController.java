package com.biblioteca.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblioteca.service.AppException;
import com.biblioteca.service.LectorService;
import com.biblioteca.service.LibroService;
import com.biblioteca.service.PrestamoService;

@Controller
public class PrestamoController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final LectorService lectorService;

    public PrestamoController(PrestamoService prestamoService, LibroService libroService,
            LectorService lectorService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.lectorService = lectorService;
    }

    @GetMapping("/prestamos")
    public String listar(Model model) {
        model.addAttribute("prestamos", prestamoService.listar());
        return "prestamos/lista";
    }

    @GetMapping("/prestamos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libros", libroService.listarDisponibles());
        model.addAttribute("lectores", lectorService.listar());
        return "prestamos/form";
    }

    @PostMapping("/prestamos")
    public String registrar(@RequestParam(required = false) Long libroId,
            @RequestParam(required = false) Long lectorId, RedirectAttributes mensajes) {
        try {
            LocalDate limite = prestamoService.registrar(libroId, lectorId);
            mensajes.addFlashAttribute("mensaje",
                    "Préstamo registrado. Fecha límite de devolución: " + limite.format(FORMATO_FECHA));
            return "redirect:/prestamos";
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
            return "redirect:/prestamos/nuevo";
        }
    }

    @PostMapping("/prestamos/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes mensajes) {
        try {
            prestamoService.devolver(id);
            mensajes.addFlashAttribute("mensaje", "Devolución registrada correctamente");
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/prestamos";
    }
}
