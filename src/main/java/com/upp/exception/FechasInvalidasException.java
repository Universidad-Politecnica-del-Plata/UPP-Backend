package com.upp.exception;

public class FechasInvalidasException extends RuntimeException {

  public FechasInvalidasException() {
    super("Las fechas del cuatrimestre son inválidas.");
  }

  public FechasInvalidasException(String message) {
    super(message);
  }

  public FechasInvalidasException(String message, Throwable cause) {
    super(message, cause);
  }
}
