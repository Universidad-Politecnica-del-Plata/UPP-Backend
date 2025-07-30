package com.upp.exception;

public class CarreraExisteException extends RuntimeException {

  public CarreraExisteException() {
    super("La carrera ya existe.");
  }

  public CarreraExisteException(String message) {
    super(message);
  }

  public CarreraExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}