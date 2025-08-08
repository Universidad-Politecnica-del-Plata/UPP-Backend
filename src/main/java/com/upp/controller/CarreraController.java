package com.upp.controller;

import com.upp.dto.CarreraDTO;
import com.upp.exception.CarreraConAlumnosException;
import com.upp.exception.CarreraConPlanesException;
import com.upp.exception.CarreraExisteException;
import com.upp.exception.CarreraNoExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.service.CarreraService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carreras")
@PreAuthorize("hasRole('GESTION_ACADEMICA')")
public class CarreraController {
  private final CarreraService carreraService;

  public CarreraController(CarreraService carreraService) {
    this.carreraService = carreraService;
  }

  @PostMapping
  public ResponseEntity<?> crearCarrera(@Valid @RequestBody CarreraDTO carreraDTO) {
    try {
      CarreraDTO resultado = carreraService.crearCarrera(carreraDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

    } catch (CarreraExisteException | PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<?> modificarCarrera(
      @PathVariable String codigo, @RequestBody CarreraDTO carreraDTO) {
    try {
      CarreraDTO resultado = carreraService.modificarCarrera(codigo, carreraDTO);
      return ResponseEntity.status(HttpStatus.OK).body(resultado);

    } catch (CarreraNoExisteException | PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarCarrera(@PathVariable String codigo) {
    try {
      carreraService.eliminarCarrera(codigo);
      return ResponseEntity.status(HttpStatus.OK)
          .body(Map.of("message", "Carrera eliminada exitosamente"));

    } catch (CarreraNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    } catch (CarreraConPlanesException | CarreraConAlumnosException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<?> obtenerCarreraPorCodigo(@PathVariable String codigo) {
    try {
      CarreraDTO carrera = carreraService.obtenerCarreraPorCodigo(codigo);
      return ResponseEntity.status(HttpStatus.OK).body(carrera);

    } catch (CarreraNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping
  public ResponseEntity<List<CarreraDTO>> obtenerTodasLasCarreras() {
    List<CarreraDTO> carreras = carreraService.obtenerTodasLasCarreras();

    return ResponseEntity.ok(carreras);
  }
}
