package com.upp.exception;

public class CursoNoExisteException extends RuntimeException {

  public CursoNoExisteException() {
    super("El curso no existe.");
  }

  public CursoNoExisteException(String message) {
    super(message);
  }

  public CursoNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
