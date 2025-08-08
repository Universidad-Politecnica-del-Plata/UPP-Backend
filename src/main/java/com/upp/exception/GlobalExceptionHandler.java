package com.upp.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errores = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errores);
  }

  @ExceptionHandler({
    CarreraConPlanesException.class,
    CarreraConAlumnosException.class,
    PlanConMateriasException.class
  })
  public ResponseEntity<Map<String, String>> handleDeletionConstraintExceptions(
      RuntimeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler({
    CarreraNoExisteException.class,
    PlanDeEstudiosNoExisteException.class,
    MateriaNoExisteException.class
  })
  public ResponseEntity<Map<String, String>> handleEntityNotFoundExceptions(RuntimeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler({
    CarreraExisteException.class,
    PlanDeEstudiosExisteException.class,
    MateriaExisteException.class
  })
  public ResponseEntity<Map<String, String>> handleEntityAlreadyExistsExceptions(
      RuntimeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }
}
