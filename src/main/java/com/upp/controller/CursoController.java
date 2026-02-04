package com.upp.controller;

import com.upp.dto.CursoDTO;
import com.upp.service.CursoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
@PreAuthorize("hasRole('GESTOR_DE_PLANIFICACION')")
public class CursoController {
  private final CursoService cursoService;

  public CursoController(CursoService cursoService) {
    this.cursoService = cursoService;
  }

  @PostMapping
  public ResponseEntity<?> crearCurso(@Valid @RequestBody CursoDTO cursoDTO) {
    CursoDTO resultado = cursoService.crearCurso(cursoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<?> modificarCurso(
      @PathVariable String codigo, @RequestBody CursoDTO cursoDTO) {
    CursoDTO resultado = cursoService.modificarCurso(codigo, cursoDTO);
    return ResponseEntity.status(HttpStatus.OK).body(resultado);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<?> eliminarCurso(@PathVariable String codigo) {
    cursoService.eliminarCurso(codigo);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Curso eliminado exitosamente"));
  }

  @GetMapping("/{codigo}")
  @PreAuthorize("hasRole('GESTOR_DE_PLANIFICACION') or hasRole('DOCENTE') or hasRole('ALUMNO')")
  public ResponseEntity<?> obtenerCursoPorCodigo(@PathVariable String codigo) {
    CursoDTO curso = cursoService.obtenerCursoPorCodigo(codigo);
    return ResponseEntity.status(HttpStatus.OK).body(curso);
  }

  @GetMapping
  @PreAuthorize("hasRole('GESTOR_DE_PLANIFICACION') or hasRole('DOCENTE')")
  public ResponseEntity<List<CursoDTO>> obtenerTodosLosCursos() {
    List<CursoDTO> cursos = cursoService.obtenerTodosLosCursos();
    return ResponseEntity.ok(cursos);
  }

  @GetMapping("/materia/{codigoMateria}")
  public ResponseEntity<List<CursoDTO>> obtenerCursosPorMateria(
      @PathVariable String codigoMateria) {
    List<CursoDTO> cursos = cursoService.obtenerCursosPorMateria(codigoMateria);
    return ResponseEntity.ok(cursos);
  }

  @GetMapping("/planDeEstudios/{codigoPlan}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<CursoDTO>> obtenerCursosPorPlanDeEstudios(
      @PathVariable String codigoPlan) {
    List<CursoDTO> cursos = cursoService.obtenerCursosPorPlanDeEstudios(codigoPlan);
    return ResponseEntity.ok(cursos);
  }
}
