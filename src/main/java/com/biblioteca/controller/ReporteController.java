package com.biblioteca.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.service.ReporteService;

@RestController
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/reportes/prestamos-pendientes")
    public ResponseEntity<byte[]> prestamosPendientes() {
        byte[] pdf = reporteService.generarPrestamosPendientesPdf();
        ContentDisposition disposicion = ContentDisposition.inline()
                .filename("prestamos-pendientes.pdf")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposicion.toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
