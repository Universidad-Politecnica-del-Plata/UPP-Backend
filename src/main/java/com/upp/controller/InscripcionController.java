package com.upp.controller;

import com.upp.dto.InscripcionDTO;
import com.upp.dto.InscripcionRequestDTO;
import com.upp.service.InscripcionService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscripciones")
@PreAuthorize("isAuthenticated()")
public class InscripcionController {
  
  private final InscripcionService inscripcionService;
  
  public InscripcionController(InscripcionService inscripcionService) {
    this.inscripcionService = inscripcionService;
  }
  
  @PostMapping
  @PreAuthorize("hasRole('ALUMNO') or hasRole('GESTOR_DE_PLANIFICACION')")
  public ResponseEntity<?> crearInscripcion(@Valid @RequestBody InscripcionRequestDTO inscripcionRequestDTO, Principal principal) {
    InscripcionDTO resultado = inscripcionService.crearInscripcion(inscripcionRequestDTO, principal.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }
  
  @DeleteMapping("/{codigoDeInscripcion}")
  @PreAuthorize("hasRole('ALUMNO') or hasRole('GESTOR_DE_PLANIFICACION')")
  public ResponseEntity<?> eliminarInscripcion(@PathVariable Long codigoDeInscripcion) {
    inscripcionService.eliminarInscripcion(codigoDeInscripcion);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Inscripción eliminada exitosamente"));
  }
  
  @GetMapping("/{codigoDeInscripcion}")
  @PreAuthorize("hasRole('ALUMNO') or hasRole('GESTOR_DE_PLANIFICACION')")
  public ResponseEntity<?> obtenerInscripcionPorCodigo(@PathVariable Long codigoDeInscripcion) {
    InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorCodigo(codigoDeInscripcion);
    return ResponseEntity.status(HttpStatus.OK).body(inscripcion);
  }
  
  @GetMapping("/misInscripciones")
  @PreAuthorize("hasRole('ALUMNO')")
  public ResponseEntity<List<InscripcionDTO>> obtenerMisInscripciones(Principal principal) {
    List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorUsername(principal.getName());
    return ResponseEntity.ok(inscripciones);
  }
}