package com.upp.exception;

public class PlanNoCorrespondeACarreraException extends RuntimeException {

  public PlanNoCorrespondeACarreraException() {
    super("El plan de estudios no corresponde a ninguna carrera asignada al alumno.");
  }

  public PlanNoCorrespondeACarreraException(String message) {
    super(message);
  }

  public PlanNoCorrespondeACarreraException(String message, Throwable cause) {
    super(message, cause);
  }
}
