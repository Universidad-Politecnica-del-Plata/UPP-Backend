package com.upp.controller;

import com.upp.model.Alumno;
import com.upp.repository.AlumnoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {
  private final AlumnoRepository alumnoRepository;

  public AlumnoController(AlumnoRepository alumnoRepository) {
    this.alumnoRepository = alumnoRepository;
  }

  @PostMapping
  public ResponseEntity<Alumno> crearAlumno(@RequestBody Alumno alumno) {
    boolean alumnoExistente =
        alumnoRepository.existsByDniOrEmail(alumno.getDni(), alumno.getEmail());
    if (alumnoExistente) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    Alumno guardado = alumnoRepository.save(alumno);
    return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
  }
}
