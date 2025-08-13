package com.upp.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
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
    MateriaExisteException.class,
    AlumnoExisteException.class
  })
  public ResponseEntity<Map<String, String>> handleEntityAlreadyExistsExceptions(
      RuntimeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
  }

  @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handleTransactionSystemException (AuthenticationException ex){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede guardar con esas caracteristicas");
    }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
