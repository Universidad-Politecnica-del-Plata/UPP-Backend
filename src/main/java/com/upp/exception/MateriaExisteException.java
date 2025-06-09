package com.upp.exception;

public class MateriaExisteException extends RuntimeException {

    public MateriaExisteException() {
        super("La materia ya existe.");
    }

    public MateriaExisteException(String message) {
        super(message);
    }

    public MateriaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
