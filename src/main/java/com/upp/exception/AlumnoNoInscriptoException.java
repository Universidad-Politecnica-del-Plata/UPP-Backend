package com.upp.exception;

public class AlumnoNoInscriptoException extends RuntimeException {

  public AlumnoNoInscriptoException() {
    super("El alumno no está inscripto en este curso para el cuatrimestre actual.");
  }

  public AlumnoNoInscriptoException(String message) {
    super(message);
  }

  public AlumnoNoInscriptoException(String message, Throwable cause) {
    super(message, cause);
  }
}
