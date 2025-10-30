package com.upp.exception;

public class CursoExisteException extends RuntimeException {

  public CursoExisteException() {
    super("El curso ya existe.");
  }

  public CursoExisteException(String message) {
    super(message);
  }

  public CursoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
