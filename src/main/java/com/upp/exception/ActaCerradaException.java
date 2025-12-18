package com.upp.exception;

public class ActaCerradaException extends RuntimeException {

  public ActaCerradaException() {
    super("No se pueden realizar modificaciones en un acta cerrada.");
  }

  public ActaCerradaException(String message) {
    super(message);
  }

  public ActaCerradaException(String message, Throwable cause) {
    super(message, cause);
  }
}
