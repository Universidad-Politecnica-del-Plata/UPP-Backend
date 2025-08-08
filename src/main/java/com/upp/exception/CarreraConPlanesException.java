package com.upp.exception;

public class CarreraConPlanesException extends RuntimeException {

  public CarreraConPlanesException() {
    super("No se puede eliminar la carrera porque tiene planes de estudio asociados.");
  }

  public CarreraConPlanesException(String message) {
    super(message);
  }

  public CarreraConPlanesException(String message, Throwable cause) {
    super(message, cause);
  }
}