package com.upp.exception;

public class AlumnoExisteException extends RuntimeException {

  public AlumnoExisteException() {
    super("El alumno ya existe.");
  }

  public AlumnoExisteException(String message) {
    super(message);
  }

  public AlumnoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
