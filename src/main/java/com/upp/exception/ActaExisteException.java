package com.upp.exception;

public class ActaExisteException extends RuntimeException {

  public ActaExisteException() {
    super("Ya existe un acta con esas características.");
  }

  public ActaExisteException(String message) {
    super(message);
  }

  public ActaExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
