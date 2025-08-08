package com.upp.exception;

public class PlanConMateriasException extends RuntimeException {

  public PlanConMateriasException() {
    super("No se puede eliminar el plan de estudios porque tiene materias asociadas.");
  }

  public PlanConMateriasException(String message) {
    super(message);
  }

  public PlanConMateriasException(String message, Throwable cause) {
    super(message, cause);
  }
}
