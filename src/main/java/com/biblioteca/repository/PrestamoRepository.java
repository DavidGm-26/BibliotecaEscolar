package com.biblioteca.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @EntityGraph(attributePaths = {"libro", "lector"})
    List<Prestamo> findAllByOrderByFechaPrestamoDescIdDesc();

    @EntityGraph(attributePaths = {"libro"})
    List<Prestamo> findByLectorIdOrderByFechaPrestamoDescIdDesc(Long lectorId);

    boolean existsByLibroId(Long libroId);

    boolean existsByLectorId(Long lectorId);

    boolean existsByLibroIdAndLectorIdAndFechaDevolucionIsNull(Long libroId, Long lectorId);

    boolean existsByLectorIdAndFechaDevolucionIsNullAndFechaLimiteBefore(Long lectorId, LocalDate fecha);

    long countByLectorIdAndFechaDevolucionIsNull(Long lectorId);
}
