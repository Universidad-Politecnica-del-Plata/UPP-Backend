package com.upp.controller;

import com.upp.dto.ActaDTO;
import com.upp.dto.ActaRequestDTO;
import com.upp.dto.AlumnoInscriptoDTO;
import com.upp.dto.EstadoActaRequestDTO;
import com.upp.dto.NotaDTO;
import com.upp.dto.NotaRequestDTO;
import com.upp.dto.NotasMasivasRequestDTO;
import com.upp.service.ActaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actas")
@PreAuthorize("isAuthenticated()")
public class ActaController {

  private final ActaService actaService;

  public ActaController(ActaService actaService) {
    this.actaService = actaService;
  }

  @PostMapping
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<ActaDTO> crearActa(@Valid @RequestBody ActaRequestDTO actaRequestDTO) {
    ActaDTO resultado = actaService.crearActa(actaRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @GetMapping("/{numeroCorrelativo}")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<ActaDTO> obtenerActaPorId(@PathVariable Long numeroCorrelativo) {
    ActaDTO acta = actaService.obtenerActaPorId(numeroCorrelativo);
    return ResponseEntity.ok(acta);
  }

  @GetMapping
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<List<ActaDTO>> obtenerTodasLasActas() {
    List<ActaDTO> actas = actaService.obtenerTodasLasActas();
    return ResponseEntity.ok(actas);
  }

  @GetMapping("/curso/{codigoCurso}")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<List<ActaDTO>> obtenerActasPorCurso(@PathVariable String codigoCurso) {
    List<ActaDTO> actas = actaService.obtenerActasPorCurso(codigoCurso);
    return ResponseEntity.ok(actas);
  }

  @PutMapping("/{numeroCorrelativo}/estado")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<ActaDTO> actualizarEstadoActa(
      @PathVariable Long numeroCorrelativo,
      @Valid @RequestBody EstadoActaRequestDTO estadoRequestDTO) {
    ActaDTO resultado = actaService.actualizarEstadoActa(numeroCorrelativo, estadoRequestDTO);
    return ResponseEntity.ok(resultado);
  }

  @PostMapping("/{numeroCorrelativo}/notas")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<NotaDTO> agregarNota(
      @PathVariable Long numeroCorrelativo, @Valid @RequestBody NotaRequestDTO notaRequestDTO) {
    NotaDTO resultado = actaService.agregarNota(numeroCorrelativo, notaRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @GetMapping("/{numeroCorrelativo}/notas")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<List<NotaDTO>> obtenerNotasPorActa(@PathVariable Long numeroCorrelativo) {
    List<NotaDTO> notas = actaService.obtenerNotasPorActa(numeroCorrelativo);
    return ResponseEntity.ok(notas);
  }

  @PutMapping("/notas/{notaId}")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<NotaDTO> actualizarNota(
      @PathVariable Long notaId, @Valid @RequestBody NotaRequestDTO notaRequestDTO) {
    NotaDTO resultado = actaService.actualizarNota(notaId, notaRequestDTO);
    return ResponseEntity.ok(resultado);
  }

  @DeleteMapping("/notas/{notaId}")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<?> eliminarNota(@PathVariable Long notaId) {
    actaService.eliminarNota(notaId);
    return ResponseEntity.ok(Map.of("message", "Nota eliminada exitosamente"));
  }

  @PostMapping("/{numeroCorrelativo}/notas/masivas")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<List<NotaDTO>> agregarNotasMasivas(
      @PathVariable Long numeroCorrelativo,
      @Valid @RequestBody NotasMasivasRequestDTO notasMasivasRequestDTO) {
    List<NotaDTO> resultado =
        actaService.agregarNotasMasivas(numeroCorrelativo, notasMasivasRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @GetMapping("/{numeroCorrelativo}/alumnos-inscriptos")
  @PreAuthorize("hasRole('DOCENTE')")
  public ResponseEntity<List<AlumnoInscriptoDTO>> obtenerAlumnosInscriptosPorActa(
      @PathVariable Long numeroCorrelativo) {
    List<AlumnoInscriptoDTO> alumnos =
        actaService.obtenerAlumnosInscriptosPorActa(numeroCorrelativo);
    return ResponseEntity.ok(alumnos);
  }
}
