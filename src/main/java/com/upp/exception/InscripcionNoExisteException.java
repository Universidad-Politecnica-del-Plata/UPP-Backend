package com.upp.exception;

public class InscripcionNoExisteException extends RuntimeException {

  public InscripcionNoExisteException() {
    super("La inscripción no existe.");
  }

  public InscripcionNoExisteException(String message) {
    super(message);
  }

  public InscripcionNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}