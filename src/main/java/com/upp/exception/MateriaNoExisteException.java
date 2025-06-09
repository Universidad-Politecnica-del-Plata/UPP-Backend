package com.upp.exception;

public class MateriaNoExisteException extends RuntimeException {

    public MateriaNoExisteException() {
        super("La materia no existe.");
    }

    public MateriaNoExisteException(String message) {
        super(message);
    }

    public MateriaNoExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
