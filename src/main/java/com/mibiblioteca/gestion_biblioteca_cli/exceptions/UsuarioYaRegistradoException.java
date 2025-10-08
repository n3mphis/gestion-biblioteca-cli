package com.mibiblioteca.gestion_biblioteca_cli.exceptions;

public class UsuarioYaRegistradoException extends RuntimeException {
    public UsuarioYaRegistradoException(String message) {
        super(message);
    }
}
