package com.upp.exception;

public class CarreraNoExisteException extends RuntimeException {

  public CarreraNoExisteException() {
    super("La carrera no existe.");
  }

  public CarreraNoExisteException(String message) {
    super(message);
  }

  public CarreraNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}