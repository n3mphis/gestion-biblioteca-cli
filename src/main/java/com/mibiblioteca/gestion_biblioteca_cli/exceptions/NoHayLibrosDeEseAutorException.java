package com.mibiblioteca.gestion_biblioteca_cli.exceptions;

public class NoHayLibrosDeEseAutorException extends RuntimeException {
    public NoHayLibrosDeEseAutorException(String message) {
        super(message);
    }
}
