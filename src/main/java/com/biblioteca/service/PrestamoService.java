package com.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.model.Lector;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.repository.LectorRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;

@Service
@Transactional
public class PrestamoService {

    private static final int DIAS_DE_PRESTAMO = 7;

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final LectorRepository lectorRepository;

    public PrestamoService(PrestamoRepository prestamoRepository, LibroRepository libroRepository,
            LectorRepository lectorRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.lectorRepository = lectorRepository;
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listar() {
        return prestamoRepository.findAllByOrderByFechaPrestamoDescIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPorLector(Long lectorId) {
        return prestamoRepository.findByLectorIdOrderByFechaPrestamoDescIdDesc(lectorId);
    }

    public LocalDate registrar(Long libroId, Long lectorId) {
        if (libroId == null || lectorId == null) {
            throw new AppException("Seleccione un libro y un lector");
        }
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new AppException("El libro seleccionado no existe"));
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new AppException("El lector seleccionado no existe"));
        if (libro.getEjemplaresDisponibles() < 1) {
            throw new AppException("No hay ejemplares disponibles de «" + libro.getTitulo() + "»");
        }
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(DIAS_DE_PRESTAMO);
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        prestamoRepository.save(new Prestamo(libro, lector, hoy, limite));
        return limite;
    }

    public void devolver(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new AppException("El préstamo solicitado no existe"));
        if (prestamo.getFechaDevolucion() != null) {
            throw new AppException("Este préstamo ya fue devuelto");
        }
        prestamo.setFechaDevolucion(LocalDate.now());
        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
    }
}
