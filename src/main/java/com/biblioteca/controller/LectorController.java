package com.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblioteca.model.Lector;
import com.biblioteca.service.AppException;
import com.biblioteca.service.LectorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/lectores")
public class LectorController {

    private static final String VISTA_FORMULARIO = "lectores/form";

    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lectores", lectorService.listar());
        return "lectores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("lector", new Lector());
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("lector", lectorService.buscar(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("lector") Lector lector, BindingResult resultado, Model model,
            RedirectAttributes mensajes) {
        if (resultado.hasErrors()) {
            return VISTA_FORMULARIO;
        }
        try {
            lectorService.guardar(lector);
        } catch (AppException e) {
            model.addAttribute("error", e.getMessage());
            return VISTA_FORMULARIO;
        }
        mensajes.addFlashAttribute("mensaje", "Lector guardado correctamente");
        return "redirect:/lectores";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes mensajes) {
        try {
            lectorService.eliminar(id);
            mensajes.addFlashAttribute("mensaje", "Lector eliminado correctamente");
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lectores";
    }
}
