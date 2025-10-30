package com.upp.exception;

public class InscripcionExisteException extends RuntimeException {

  public InscripcionExisteException() {
    super("La inscripción ya existe.");
  }

  public InscripcionExisteException(String message) {
    super(message);
  }

  public InscripcionExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}