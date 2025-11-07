package com.upp.exception;

public class NotaNoExisteException extends RuntimeException {

  public NotaNoExisteException() {
    super("La nota no existe.");
  }

  public NotaNoExisteException(String message) {
    super(message);
  }

  public NotaNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}