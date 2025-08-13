package com.upp.controller;

import com.upp.dto.CarreraDTO;
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
    CarreraDTO resultado = carreraService.crearCarrera(carreraDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<?> modificarCarrera(
      @PathVariable String codigo, @RequestBody CarreraDTO carreraDTO) {
    CarreraDTO resultado = carreraService.modificarCarrera(codigo, carreraDTO);
    return ResponseEntity.status(HttpStatus.OK).body(resultado);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarCarrera(@PathVariable String codigo) {
    carreraService.eliminarCarrera(codigo);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Carrera eliminada exitosamente"));
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<?> obtenerCarreraPorCodigo(@PathVariable String codigo) {
    CarreraDTO carrera = carreraService.obtenerCarreraPorCodigo(codigo);
    return ResponseEntity.status(HttpStatus.OK).body(carrera);
  }

  @GetMapping
  public ResponseEntity<List<CarreraDTO>> obtenerTodasLasCarreras() {
    List<CarreraDTO> carreras = carreraService.obtenerTodasLasCarreras();

    return ResponseEntity.ok(carreras);
  }
}
