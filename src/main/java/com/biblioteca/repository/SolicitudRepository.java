package com.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.model.SolicitudPrestamo;

public interface SolicitudRepository extends JpaRepository<SolicitudPrestamo, Long> {

    @EntityGraph(attributePaths = {"libro", "lector"})
    List<SolicitudPrestamo> findAllByOrderByFechaSolicitudDescIdDesc();

    @EntityGraph(attributePaths = {"libro"})
    List<SolicitudPrestamo> findByLectorIdOrderByFechaSolicitudDescIdDesc(Long lectorId);
}
