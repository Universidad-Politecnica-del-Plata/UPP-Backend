package com.upp.controller;

import com.upp.dto.EstadoAcademicoDTO;
import com.upp.dto.HistoriaAcademicaDTO;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.model.Alumno;
import com.upp.model.Materia;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.MateriaRepository;
import com.upp.service.HistoriaAcademicaService;
import java.security.Principal;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historia-academica")
public class HistoriaAcademicaController {

  private final HistoriaAcademicaService historiaAcademicaService;
  private final AlumnoRepository alumnoRepository;
  private final MateriaRepository materiaRepository;

  public HistoriaAcademicaController(
      HistoriaAcademicaService historiaAcademicaService,
      AlumnoRepository alumnoRepository,
      MateriaRepository materiaRepository) {
    this.historiaAcademicaService = historiaAcademicaService;
    this.alumnoRepository = alumnoRepository;
    this.materiaRepository = materiaRepository;
  }

  @GetMapping("/mi-historia")
  @PreAuthorize("hasRole('ALUMNO')")
  public ResponseEntity<HistoriaAcademicaDTO> obtenerMiHistoriaAcademica(Principal principal) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByUsername(principal.getName());
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No se encontró el alumno actual.");
    }

    HistoriaAcademicaDTO historia = historiaAcademicaService.obtenerHistoriaAcademica(alumnoOpt.get());
    return ResponseEntity.ok(historia);
  }

  @GetMapping("/alumno/{alumnoId}")
  @PreAuthorize("hasRole('DOCENTE') or hasRole('SECRETARIA_DE_PLANIFICACION')")
  public ResponseEntity<HistoriaAcademicaDTO> obtenerHistoriaAcademicaPorId(@PathVariable Long alumnoId) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No se encontró el alumno con ID: " + alumnoId);
    }

    HistoriaAcademicaDTO historia = historiaAcademicaService.obtenerHistoriaAcademica(alumnoOpt.get());
    return ResponseEntity.ok(historia);
  }

  @GetMapping("/estado-inscripcion")
  @PreAuthorize("hasRole('ALUMNO')")
  public ResponseEntity<EstadoAcademicoDTO> evaluarEstadoParaInscripcion(
      @RequestParam String codigoMateria,
      Principal principal) {
    
    Optional<Alumno> alumnoOpt = alumnoRepository.findByUsername(principal.getName());
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No se encontró el alumno actual.");
    }

    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigoMateria);
    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No se encontró la materia con código: " + codigoMateria);
    }

    EstadoAcademicoDTO estado = historiaAcademicaService.evaluarEstadoParaInscripcion(
        alumnoOpt.get(), materiaOpt.get());
    
    return ResponseEntity.ok(estado);
  }
}