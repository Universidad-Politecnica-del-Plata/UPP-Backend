package com.upp.exception;

public class CuatrimestreNoExisteException extends RuntimeException {

  public CuatrimestreNoExisteException() {
    super("El cuatrimestre no existe.");
  }

  public CuatrimestreNoExisteException(String message) {
    super(message);
  }

  public CuatrimestreNoExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}