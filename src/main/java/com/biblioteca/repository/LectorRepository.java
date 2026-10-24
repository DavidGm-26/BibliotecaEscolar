package com.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.model.Lector;

public interface LectorRepository extends JpaRepository<Lector, Long> {

    List<Lector> findAllByOrderByNombreAsc();

    Optional<Lector> findByCorreo(String correo);
}
