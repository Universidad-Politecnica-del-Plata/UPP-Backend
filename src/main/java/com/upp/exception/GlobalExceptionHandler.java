package com.upp.exception;

import jakarta.validation.ConstraintViolationException;
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
    PlanConMateriasException.class,
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
    MateriaNoExisteException.class,
    AlumnoNoExisteException.class,
    CursoNoExisteException.class,
    CuatrimestreNoExisteException.class,
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
    AlumnoExisteException.class,
    CursoExisteException.class,
    CuatrimestreExisteException.class,
    InscripcionExisteException.class,
  })
  public ResponseEntity<Map<String, String>> handleEntityAlreadyExistsExceptions(
      RuntimeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Map<String, String>> handleAuthenticationException(
      AuthenticationException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Credenciales inválidas");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(TransactionSystemException.class)
  public ResponseEntity<Map<String, String>> handleTransactionSystemException(
      TransactionSystemException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "No se puede guardar con esas caracteristicas");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "No se puede guardar con campos vacios");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
