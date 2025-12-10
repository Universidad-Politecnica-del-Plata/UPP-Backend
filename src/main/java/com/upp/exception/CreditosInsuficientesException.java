package com.upp.exception;

public class CreditosInsuficientesException extends RuntimeException {
  public CreditosInsuficientesException(String message) {
    super(message);
  }

  public CreditosInsuficientesException(String message, Throwable cause) {
    super(message, cause);
  }
}