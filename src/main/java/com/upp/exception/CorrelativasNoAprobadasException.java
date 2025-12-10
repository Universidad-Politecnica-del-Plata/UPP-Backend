package com.upp.exception;

public class CorrelativasNoAprobadasException extends RuntimeException {
  public CorrelativasNoAprobadasException(String message) {
    super(message);
  }

  public CorrelativasNoAprobadasException(String message, Throwable cause) {
    super(message, cause);
  }
}