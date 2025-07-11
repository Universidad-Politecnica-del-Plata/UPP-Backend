package com.upp.controller;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.exception.MateriaNoExisteException;
import com.upp.exception.PlanDeEstudiosExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.service.PlanDeEstudiosService;
import jakarta.validation.Valid;
import java.util.List;
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
  public ResponseEntity<PlanDeEstudiosResponseDTO> crearPlanDeEstudios(
      @Valid @RequestBody PlanDeEstudiosRequestDTO planDeEstudiosRequestDTO) {
    try {
      PlanDeEstudiosResponseDTO resultado =
          planDeEstudiosService.crearPlanDeEstudios(planDeEstudiosRequestDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

    } catch (MateriaNoExisteException | PlanDeEstudiosExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<PlanDeEstudiosResponseDTO> obtenerPlanDeEstudiosPorCodigo(
      @PathVariable String codigo) {
    try {
      PlanDeEstudiosResponseDTO planDeEstudios =
          planDeEstudiosService.obtenerPlanDeEstudiosPorCodigo(codigo);
      return ResponseEntity.status(HttpStatus.OK).body(planDeEstudios);

    } catch (PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<PlanDeEstudiosResponseDTO>> obtenerTodosLosPlanesDeEstudio() {
    List<PlanDeEstudiosResponseDTO> planes =
        planDeEstudiosService.obtenerTodosLosPlanesDeEstudios();

    return ResponseEntity.ok(planes);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> eliminarPlanDeEstudios(@PathVariable String codigo) {
    try {
      planDeEstudiosService.eliminarPlanDeEstudios(codigo);
      return ResponseEntity.status(HttpStatus.OK).build();

    } catch (PlanDeEstudiosNoExisteException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
