package com.mibiblioteca.gestion_biblioteca_cli.exceptions;

public class LibroYaRegistradoException extends RuntimeException {
    public LibroYaRegistradoException(String message) {
        super(message);
    }
}
