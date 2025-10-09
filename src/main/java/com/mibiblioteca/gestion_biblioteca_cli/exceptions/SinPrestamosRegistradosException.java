package com.mibiblioteca.gestion_biblioteca_cli.exceptions;

public class SinPrestamosRegistradosException extends RuntimeException {
    public SinPrestamosRegistradosException(String message) {
        super(message);
    }
}
