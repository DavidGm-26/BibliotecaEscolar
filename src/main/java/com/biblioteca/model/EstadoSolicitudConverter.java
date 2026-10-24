package com.biblioteca.model;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoSolicitudConverter implements AttributeConverter<EstadoSolicitud, String> {

    @Override
    public String convertToDatabaseColumn(EstadoSolicitud estado) {
        return estado == null ? null : estado.name();
    }

    @Override
    public EstadoSolicitud convertToEntityAttribute(String valor) {
        return valor == null ? null : EstadoSolicitud.valueOf(valor.trim().toUpperCase(Locale.ROOT));
    }
}
