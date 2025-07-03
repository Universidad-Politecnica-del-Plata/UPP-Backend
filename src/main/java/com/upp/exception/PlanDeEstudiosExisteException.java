package com.upp.exception;

public class PlanDeEstudiosExisteException extends RuntimeException {

  public PlanDeEstudiosExisteException() {
    super("El plan de estudios ya existe.");
  }

  public PlanDeEstudiosExisteException(String message) {
    super(message);
  }

  public PlanDeEstudiosExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
