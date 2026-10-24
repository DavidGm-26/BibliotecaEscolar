package com.biblioteca.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.model.Lector;
import com.biblioteca.model.Libro;
import com.biblioteca.model.SolicitudPrestamo;
import com.biblioteca.repository.LectorRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.SolicitudRepository;

@Service
@Transactional
public class SolicitudService {

    private static final int MAXIMO_ACTIVOS = 3;
    private static final int LARGO_MAXIMO_MOTIVO = 200;
    private static final String MOTIVO_POR_DEFECTO = "Rechazada por el administrador";

    private final SolicitudRepository solicitudRepository;
    private final LibroRepository libroRepository;
    private final LectorRepository lectorRepository;
    private final PrestamoRepository prestamoRepository;
    private final PrestamoService prestamoService;

    public SolicitudService(SolicitudRepository solicitudRepository, LibroRepository libroRepository,
            LectorRepository lectorRepository, PrestamoRepository prestamoRepository,
            PrestamoService prestamoService) {
        this.solicitudRepository = solicitudRepository;
        this.libroRepository = libroRepository;
        this.lectorRepository = lectorRepository;
        this.prestamoRepository = prestamoRepository;
        this.prestamoService = prestamoService;
    }

    @Transactional(readOnly = true)
    public List<SolicitudPrestamo> listarTodas() {
        return solicitudRepository.findAllByOrderByFechaSolicitudDescIdDesc();
    }

    @Transactional(readOnly = true)
    public List<SolicitudPrestamo> listarPorLector(Long lectorId) {
        return solicitudRepository.findByLectorIdOrderByFechaSolicitudDescIdDesc(lectorId);
    }

    @Transactional(readOnly = true)
    public Set<Long> idsLibrosPendientes(Long lectorId) {
        return pendientesDe(lectorId).stream()
                .map(solicitud -> solicitud.getLibro().getId())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public long contarPendientes() {
        return solicitudRepository.findAll().stream().filter(SolicitudPrestamo::isPendiente).count();
    }

    public void solicitar(Long lectorId, Long libroId) {
        if (lectorId == null) {
            throw new AppException("Solo los alumnos pueden solicitar préstamos");
        }
        if (libroId == null) {
            throw new AppException("Seleccione un libro");
        }
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new AppException("El lector no existe"));
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new AppException("El libro seleccionado no existe"));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new AppException("«" + libro.getTitulo() + "» no tiene ejemplares disponibles por ahora");
        }
        List<SolicitudPrestamo> pendientes = pendientesDe(lectorId);
        if (pendientes.stream().anyMatch(solicitud -> solicitud.getLibro().getId().equals(libroId))) {
            throw new AppException("Ya tiene una solicitud pendiente para «" + libro.getTitulo() + "»");
        }
        if (prestamoRepository.existsByLibroIdAndLectorIdAndFechaDevolucionIsNull(libroId, lectorId)) {
            throw new AppException("Ya tiene un préstamo activo de «" + libro.getTitulo() + "»");
        }
        if (prestamoRepository.existsByLectorIdAndFechaDevolucionIsNullAndFechaLimiteBefore(lectorId,
                LocalDate.now())) {
            throw new AppException("Tiene préstamos vencidos; devuélvalos antes de solicitar otro libro");
        }
        long comprometidos = prestamoRepository.countByLectorIdAndFechaDevolucionIsNull(lectorId)
                + pendientes.size();
        if (comprometidos >= MAXIMO_ACTIVOS) {
            throw new AppException("Alcanzó el máximo de " + MAXIMO_ACTIVOS
                    + " préstamos y solicitudes pendientes a la vez");
        }
        solicitudRepository.save(new SolicitudPrestamo(libro, lector, LocalDate.now()));
    }

    public void aprobar(Long id) {
        SolicitudPrestamo solicitud = buscarPendiente(id);
        prestamoService.registrar(solicitud.getLibro().getId(), solicitud.getLector().getId());
        solicitud.aprobar(LocalDate.now());
    }

    public void rechazar(Long id, String motivo) {
        SolicitudPrestamo solicitud = buscarPendiente(id);
        String texto = motivo == null ? "" : motivo.trim();
        if (texto.length() > LARGO_MAXIMO_MOTIVO) {
            throw new AppException("El motivo admite hasta " + LARGO_MAXIMO_MOTIVO + " caracteres");
        }
        solicitud.rechazar(LocalDate.now(), texto.isEmpty() ? MOTIVO_POR_DEFECTO : texto);
    }

    private List<SolicitudPrestamo> pendientesDe(Long lectorId) {
        return solicitudRepository.findByLectorIdOrderByFechaSolicitudDescIdDesc(lectorId).stream()
                .filter(SolicitudPrestamo::isPendiente)
                .toList();
    }

    private SolicitudPrestamo buscarPendiente(Long id) {
        SolicitudPrestamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new AppException("La solicitud no existe"));
        if (!solicitud.isPendiente()) {
            throw new AppException("Esta solicitud ya fue resuelta");
        }
        return solicitud;
    }
}
