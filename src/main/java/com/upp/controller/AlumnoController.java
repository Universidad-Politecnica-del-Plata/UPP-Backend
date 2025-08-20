package com.upp.controller;

import com.upp.dto.AlumnoDTO;
import com.upp.service.AlumnoService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('GESTION_ESTUDIANTIL')")
@RequestMapping("/api/alumnos")
public class AlumnoController {
  private final AlumnoService alumnoService;

  public AlumnoController(AlumnoService alumnoService) {
    this.alumnoService = alumnoService;
  }

  @PostMapping
  public ResponseEntity<?> crearAlumno(@RequestBody AlumnoDTO alumnoDTO) {
    AlumnoDTO alumnoCreado = alumnoService.crearAlumno(alumnoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(alumnoCreado);
  }

  @GetMapping
  public ResponseEntity<List<AlumnoDTO>> obtenerTodosLosAlumnos() {
    List<AlumnoDTO> alumnos = alumnoService.obtenerTodosLosAlumnos();
    return ResponseEntity.ok(alumnos);
  }

  @GetMapping("/{matricula}")
  public ResponseEntity<?> obtenerAlumnoPorMatricula(@PathVariable Long matricula) {
    AlumnoDTO alumno = alumnoService.obtenerAlumnoPorMatricula(matricula);
    return ResponseEntity.status(HttpStatus.OK).body(alumno);
  }

  @PutMapping("/{matricula}")
  public ResponseEntity<?> modificarAlumno(
      @PathVariable Long matricula, @RequestBody AlumnoDTO alumnoDTO) {
    AlumnoDTO resultado = alumnoService.modificarAlumno(matricula, alumnoDTO);
    return ResponseEntity.status(HttpStatus.OK).body(resultado);
  }

  @DeleteMapping("/{matricula}")
  public ResponseEntity<?> eliminarAlumno(@PathVariable Long matricula) {
    alumnoService.eliminarAlumno(matricula);
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("message", "Alumno dado de baja exitosamente"));
  }
}
