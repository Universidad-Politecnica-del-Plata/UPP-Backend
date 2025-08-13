package com.upp.controller;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
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
    PlanDeEstudiosResponseDTO resultado =
        planDeEstudiosService.crearPlanDeEstudios(planDeEstudiosRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<?> obtenerPlanDeEstudiosPorCodigo(@PathVariable String codigo) {
    PlanDeEstudiosResponseDTO planDeEstudios =
        planDeEstudiosService.obtenerPlanDeEstudiosPorCodigo(codigo);
    return ResponseEntity.status(HttpStatus.OK).body(planDeEstudios);
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
    PlanDeEstudiosResponseDTO resultado =
        planDeEstudiosService.modificarPlanDeEstudios(codigo, planDeEstudiosRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(resultado);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarPlanDeEstudios(@PathVariable String codigo) {
    planDeEstudiosService.eliminarPlanDeEstudios(codigo);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Plan de estudios eliminado exitosamente"));
  }
}
