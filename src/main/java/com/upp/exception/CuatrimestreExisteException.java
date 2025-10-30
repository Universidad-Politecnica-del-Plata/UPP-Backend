package com.upp.exception;

public class CuatrimestreExisteException extends RuntimeException {

  public CuatrimestreExisteException() {
    super("El cuatrimestre ya existe.");
  }

  public CuatrimestreExisteException(String message) {
    super(message);
  }

  public CuatrimestreExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
