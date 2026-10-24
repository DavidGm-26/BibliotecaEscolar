package com.biblioteca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.service.AppException;

@ControllerAdvice
public class ManejadorErrores {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ManejadorErrores.class);
    private static final String VISTA_ERROR = "pagina-error";

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarErrorControlado(AppException e, Model model) {
        model.addAttribute("mensaje", e.getMessage());
        return VISTA_ERROR;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String manejarErrorDeDatos(DataAccessException e, Model model) {
        REGISTRO.error("Error de acceso a datos", e);
        model.addAttribute("mensaje", "No se pudo completar la operación con los datos. Intente nuevamente");
        return VISTA_ERROR;
    }
}
