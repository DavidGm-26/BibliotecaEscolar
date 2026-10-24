package com.biblioteca.service;

public class AppException extends RuntimeException {

    public AppException(String mensaje) {
        super(mensaje);
    }
}
