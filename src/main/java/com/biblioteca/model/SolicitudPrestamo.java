package com.biblioteca.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_prestamo")
public class SolicitudPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lector_id", nullable = false)
    private Lector lector;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Convert(converter = EstadoSolicitudConverter.class)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoSolicitud estado;

    @Column(name = "fecha_resolucion")
    private LocalDate fechaResolucion;

    @Column(name = "motivo_rechazo", length = 200)
    private String motivoRechazo;

    protected SolicitudPrestamo() {
    }

    public SolicitudPrestamo(Libro libro, Lector lector, LocalDate fechaSolicitud) {
        this.libro = libro;
        this.lector = lector;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public boolean isPendiente() {
        return estado == EstadoSolicitud.PENDIENTE;
    }

    public boolean isAprobada() {
        return estado == EstadoSolicitud.APROBADA;
    }

    public boolean isRechazada() {
        return estado == EstadoSolicitud.RECHAZADA;
    }

    public String getEstadoEtiqueta() {
        return estado.getEtiqueta();
    }

    public void aprobar(LocalDate fecha) {
        this.estado = EstadoSolicitud.APROBADA;
        this.fechaResolucion = fecha;
    }

    public void rechazar(LocalDate fecha, String motivo) {
        this.estado = EstadoSolicitud.RECHAZADA;
        this.fechaResolucion = fecha;
        this.motivoRechazo = motivo;
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

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public LocalDate getFechaResolucion() {
        return fechaResolucion;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }
}
