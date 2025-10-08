package com.mibiblioteca.gestion_biblioteca_cli.exceptions;

public class SinPrestamosActivosExcepcion extends RuntimeException {
    public SinPrestamosActivosExcepcion(String message) {
        super(message);
    }
}
