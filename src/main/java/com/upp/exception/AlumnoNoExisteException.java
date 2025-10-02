package com.upp.exception;

public class AlumnoNoExisteException extends RuntimeException {

  public AlumnoNoExisteException() {
    super("El alumno no existe.");
  }

  public AlumnoNoExisteException(String message) {
    super(message);
  }

  public AlumnoNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
