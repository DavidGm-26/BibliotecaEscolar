package com.biblioteca.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class ReporteService {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ReporteService.class);
    private static final String PLANTILLA_PRESTAMOS_PENDIENTES = "reportes/prestamos_pendientes.jrxml";

    private final DataSource dataSource;

    public ReporteService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] generarPrestamosPendientesPdf() {
        try (InputStream plantilla = new ClassPathResource(PLANTILLA_PRESTAMOS_PENDIENTES).getInputStream();
                Connection conexion = dataSource.getConnection()) {
            JasperReport reporte = JasperCompileManager.compileReport(plantilla);
            JasperPrint impresion = JasperFillManager.fillReport(reporte, new HashMap<>(), conexion);
            return JasperExportManager.exportReportToPdf(impresion);
        } catch (JRException | IOException | SQLException e) {
            REGISTRO.error("No se pudo generar el reporte de préstamos pendientes", e);
            throw new AppException("No se pudo generar el reporte. Intente nuevamente más tarde");
        }
    }
}
