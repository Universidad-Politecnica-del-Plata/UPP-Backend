package com.upp.controller;

import com.upp.dto.AlumnoDTO;
import com.upp.exception.AlumnoExisteException;
import com.upp.service.AlumnoService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
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

    try {
      AlumnoDTO alumnoCreado = alumnoService.crearAlumno(alumnoDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(alumnoCreado);
    } catch (AlumnoExisteException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
  }
}
