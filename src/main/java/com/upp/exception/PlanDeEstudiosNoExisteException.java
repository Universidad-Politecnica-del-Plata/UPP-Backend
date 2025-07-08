package com.upp.exception;

public class PlanDeEstudiosNoExisteException extends RuntimeException {

  public PlanDeEstudiosNoExisteException() {
    super("El plan de estudios no existe.");
  }

  public PlanDeEstudiosNoExisteException(String message) {
    super(message);
  }

  public PlanDeEstudiosNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
