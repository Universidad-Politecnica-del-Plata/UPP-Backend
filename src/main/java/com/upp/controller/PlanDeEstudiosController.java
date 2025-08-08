package com.upp.controller;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.exception.MateriaNoExisteException;
import com.upp.exception.PlanDeEstudiosExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.exception.PlanConMateriasException;
import com.upp.service.PlanDeEstudiosService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planDeEstudios")
@PreAuthorize("hasRole('GESTION_ACADEMICA')")
public class PlanDeEstudiosController {
  private final PlanDeEstudiosService planDeEstudiosService;

  public PlanDeEstudiosController(PlanDeEstudiosService planDeEstudiosService) {
    this.planDeEstudiosService = planDeEstudiosService;
  }

  @PostMapping
  public ResponseEntity<?> crearPlanDeEstudios(
      @Valid @RequestBody PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
    try {
      PlanDeEstudiosResponseDTO resultado =
          planDeEstudiosService.crearPlanDeEstudios(planDeEstudiosRequestDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

    } catch (MateriaNoExisteException | PlanDeEstudiosExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<?> obtenerPlanDeEstudiosPorCodigo(
      @PathVariable String codigo) {
    try {
      PlanDeEstudiosResponseDTO planDeEstudios =
          planDeEstudiosService.obtenerPlanDeEstudiosPorCodigo(codigo);
      return ResponseEntity.status(HttpStatus.OK).body(planDeEstudios);

    } catch (PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping
  public ResponseEntity<List<PlanDeEstudiosResponseDTO>> obtenerTodosLosPlanesDeEstudio() {
    List<PlanDeEstudiosResponseDTO> planes =
        planDeEstudiosService.obtenerTodosLosPlanesDeEstudios();

    return ResponseEntity.ok(planes);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<?> modificarPlanDeEstudios(
      @PathVariable String codigo, @RequestBody PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {

    try {
      PlanDeEstudiosResponseDTO resultado =
          planDeEstudiosService.modificarPlanDeEstudios(codigo, planDeEstudiosRequestDTO);
      return ResponseEntity.status(HttpStatus.OK).body(resultado);

    } catch (PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    } catch (MateriaNoExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarPlanDeEstudios(@PathVariable String codigo) {
    try {
      planDeEstudiosService.eliminarPlanDeEstudios(codigo);
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Plan de estudios eliminado exitosamente"));

    } catch (PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    } catch (PlanConMateriasException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }
  }
}
