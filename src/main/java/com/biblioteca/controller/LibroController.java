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

import com.biblioteca.model.Libro;
import com.biblioteca.service.AppException;
import com.biblioteca.service.LibroService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/libros")
public class LibroController {

    private static final String VISTA_FORMULARIO = "libros/form";

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("libros", libroService.listar());
        return "libros/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libro", new Libro());
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("libro", libroService.buscar(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("libro") Libro libro, BindingResult resultado, Model model,
            RedirectAttributes mensajes) {
        if (resultado.hasErrors()) {
            return VISTA_FORMULARIO;
        }
        try {
            libroService.guardar(libro);
        } catch (AppException e) {
            model.addAttribute("error", e.getMessage());
            return VISTA_FORMULARIO;
        }
        mensajes.addFlashAttribute("mensaje", "Libro guardado correctamente");
        return "redirect:/libros";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes mensajes) {
        try {
            libroService.eliminar(id);
            mensajes.addFlashAttribute("mensaje", "Libro eliminado correctamente");
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/libros";
    }
}
