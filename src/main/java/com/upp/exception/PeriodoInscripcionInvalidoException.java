package com.upp.exception;

public class PeriodoInscripcionInvalidoException extends RuntimeException {

  public PeriodoInscripcionInvalidoException() {
    super("No se puede realizar la inscripción fuera del período permitido.");
  }

  public PeriodoInscripcionInvalidoException(String message) {
    super(message);
  }

  public PeriodoInscripcionInvalidoException(String message, Throwable cause) {
    super(message, cause);
  }
}