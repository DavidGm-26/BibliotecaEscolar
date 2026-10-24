package com.biblioteca.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblioteca.model.EstadoSolicitud;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.SolicitudPrestamo;
import com.biblioteca.seguridad.SesionUsuario;
import com.biblioteca.service.AppException;
import com.biblioteca.service.LibroService;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.service.SolicitudService;

@Controller
public class AlumnoController {

    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final SolicitudService solicitudService;

    public AlumnoController(LibroService libroService, PrestamoService prestamoService,
            SolicitudService solicitudService) {
        this.libroService = libroService;
        this.prestamoService = prestamoService;
        this.solicitudService = solicitudService;
    }

    @GetMapping("/catalogo")
    public String catalogo(@SessionAttribute(SesionUsuario.ATRIBUTO) SesionUsuario usuario,
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String categoria,
            @RequestParam(defaultValue = "false") boolean soloDisponibles, Model model) {
        boolean esAlumno = usuario.getLectorId() != null;
        Set<Long> pendientes = esAlumno ? solicitudService.idsLibrosPendientes(usuario.getLectorId()) : Set.of();
        model.addAttribute("libros", libroService.buscar(q, categoria, soloDisponibles));
        model.addAttribute("categorias", libroService.listarCategorias());
        model.addAttribute("q", q);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("soloDisponibles", soloDisponibles);
        model.addAttribute("librosPendientes", pendientes);
        model.addAttribute("puedeSolicitar", esAlumno);
        return "alumno/catalogo";
    }

    @PostMapping("/solicitudes")
    public String solicitar(@SessionAttribute(SesionUsuario.ATRIBUTO) SesionUsuario usuario,
            @RequestParam(required = false) Long libroId, RedirectAttributes mensajes) {
        try {
            solicitudService.solicitar(usuario.getLectorId(), libroId);
            mensajes.addFlashAttribute("mensaje", "Solicitud enviada. El administrador la revisará pronto");
            return "redirect:/mis-solicitudes";
        } catch (AppException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogo";
        }
    }

    @GetMapping("/mis-solicitudes")
    public String misSolicitudes(@SessionAttribute(SesionUsuario.ATRIBUTO) SesionUsuario usuario, Model model) {
        if (usuario.getLectorId() == null) {
            return "redirect:/gestion-solicitudes";
        }
        List<SolicitudPrestamo> solicitudes = solicitudService.listarPorLector(usuario.getLectorId());
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("pendientes", contar(solicitudes, EstadoSolicitud.PENDIENTE));
        model.addAttribute("aprobadas", contar(solicitudes, EstadoSolicitud.APROBADA));
        model.addAttribute("rechazadas", contar(solicitudes, EstadoSolicitud.RECHAZADA));
        return "alumno/solicitudes";
    }

    @GetMapping("/mis-prestamos")
    public String misPrestamos(@SessionAttribute(SesionUsuario.ATRIBUTO) SesionUsuario usuario, Model model) {
        if (usuario.getLectorId() == null) {
            return "redirect:/prestamos";
        }
        List<Prestamo> todos = prestamoService.listarPorLector(usuario.getLectorId());
        List<Prestamo> activos = todos.stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .sorted(Comparator.comparing(Prestamo::getFechaLimite))
                .toList();
        List<Prestamo> historial = todos.stream().filter(p -> p.getFechaDevolucion() != null).toList();
        model.addAttribute("activos", activos);
        model.addAttribute("historial", historial);
        model.addAttribute("vencidos", activos.stream().filter(Prestamo::isVencido).count());
        return "alumno/prestamos";
    }

    private long contar(List<SolicitudPrestamo> solicitudes, EstadoSolicitud estado) {
        return solicitudes.stream().filter(s -> s.getEstado() == estado).count();
    }
}
