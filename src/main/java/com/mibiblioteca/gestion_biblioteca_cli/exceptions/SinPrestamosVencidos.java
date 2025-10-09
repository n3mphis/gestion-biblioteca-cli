package com.mibiblioteca.gestion_biblioteca_cli.exceptions;

public class SinPrestamosVencidos extends RuntimeException {
    public SinPrestamosVencidos(String message) {
        super(message);
    }
}
