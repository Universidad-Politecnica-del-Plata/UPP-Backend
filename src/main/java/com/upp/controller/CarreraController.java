package com.upp.controller;

import com.upp.dto.CarreraDTO;
import com.upp.exception.CarreraExisteException;
import com.upp.exception.CarreraNoExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.service.CarreraService;
import jakarta.validation.Valid;
import java.util.List;
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
  public ResponseEntity<CarreraDTO> crearCarrera(@Valid @RequestBody CarreraDTO carreraDTO) {
    try {
      CarreraDTO resultado = carreraService.crearCarrera(carreraDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

    } catch (CarreraExisteException | PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<CarreraDTO> obtenerCarreraPorCodigo(@PathVariable String codigo) {
    try {
      CarreraDTO carrera = carreraService.obtenerCarreraPorCodigo(codigo);
      return ResponseEntity.status(HttpStatus.OK).body(carrera);

    } catch (CarreraNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<CarreraDTO>> obtenerTodasLasCarreras() {
    List<CarreraDTO> carreras = carreraService.obtenerTodasLasCarreras();

    return ResponseEntity.ok(carreras);
  }
}