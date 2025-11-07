package com.upp.exception;

public class ActaNoExisteException extends RuntimeException {

  public ActaNoExisteException() {
    super("El acta no existe.");
  }

  public ActaNoExisteException(String message) {
    super(message);
  }

  public ActaNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}