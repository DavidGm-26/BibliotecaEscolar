package com.biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lector_id", nullable = false)
    private Lector lector;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    protected Prestamo() {
    }

    public Prestamo(Libro libro, Lector lector, LocalDate fechaPrestamo, LocalDate fechaLimite) {
        this.libro = libro;
        this.lector = lector;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
    }

    public boolean isVencido() {
        return fechaDevolucion == null && fechaLimite.isBefore(LocalDate.now());
    }

    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaLimite);
    }

    public Long getId() {
        return id;
    }

    public Libro getLibro() {
        return libro;
    }

    public Lector getLector() {
        return lector;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
