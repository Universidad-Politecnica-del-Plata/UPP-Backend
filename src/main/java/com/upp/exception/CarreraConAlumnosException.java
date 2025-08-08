package com.upp.exception;

public class CarreraConAlumnosException extends RuntimeException {

  public CarreraConAlumnosException() {
    super("No se puede eliminar la carrera porque tiene alumnos inscriptos.");
  }

  public CarreraConAlumnosException(String message) {
    super(message);
  }

  public CarreraConAlumnosException(String message, Throwable cause) {
    super(message, cause);
  }
}
