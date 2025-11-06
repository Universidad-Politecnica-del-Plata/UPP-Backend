package com.upp.service;

import com.upp.dto.InscripcionDTO;
import com.upp.dto.InscripcionRequestDTO;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.InscripcionExisteException;
import com.upp.exception.InscripcionNoExisteException;
import com.upp.exception.PeriodoInscripcionInvalidoException;
import com.upp.model.Alumno;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.Inscripcion;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.CursoRepository;
import com.upp.repository.InscripcionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {

  private final InscripcionRepository inscripcionRepository;
  private final AlumnoRepository alumnoRepository;
  private final CursoRepository cursoRepository;
  private final CuatrimestreRepository cuatrimestreRepository;

  public InscripcionService(
      InscripcionRepository inscripcionRepository,
      AlumnoRepository alumnoRepository,
      CursoRepository cursoRepository,
      CuatrimestreRepository cuatrimestreRepository) {
    this.inscripcionRepository = inscripcionRepository;
    this.alumnoRepository = alumnoRepository;
    this.cursoRepository = cursoRepository;
    this.cuatrimestreRepository = cuatrimestreRepository;
  }

  public InscripcionDTO crearInscripcion(InscripcionRequestDTO inscripcionDTO, String username) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByUsername(username);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con ese username.");
    }

    Optional<Curso> cursoOpt = cursoRepository.findByCodigo(inscripcionDTO.getCodigoCurso());
    if (cursoOpt.isEmpty()) {
      throw new CursoNoExisteException("No existe un curso con ese código.");
    }

    List<Cuatrimestre> cuatrimestreOpt = cuatrimestreRepository.findCuatrimestresActuales(LocalDate.now());
    if (cuatrimestreOpt.isEmpty()) {
      throw new CuatrimestreNoExisteException("No existe un cuatrimestre con ese código.");
    }

    Alumno alumno = alumnoOpt.get();
    Curso curso = cursoOpt.get();
    Cuatrimestre cuatrimestre = cuatrimestreOpt.get(0);

    if (inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre)) {
      throw new InscripcionExisteException(
          "El alumno ya está inscrito en este curso para este cuatrimestre.");
    }

    validarPeriodoDeInscripcion(cuatrimestre);

    Inscripcion inscripcion = new Inscripcion(curso, cuatrimestre, alumno);
    inscripcionRepository.save(inscripcion);

    return new InscripcionDTO(
        inscripcion.getCodigoDeInscripcion(),
        inscripcion.getFecha(),
        inscripcion.getHorario(),
        curso.getCodigo(),
        cuatrimestre.getCodigo(),
        alumno.getId());
  }

  public void eliminarInscripcion(Long codigoDeInscripcion) {
    Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(codigoDeInscripcion);

    if (inscripcionOpt.isEmpty()) {
      throw new InscripcionNoExisteException("No existe una inscripción con ese código.");
    }

    inscripcionRepository.delete(inscripcionOpt.get());
  }

  public List<InscripcionDTO> obtenerInscripcionesPorUsername(String username) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByUsername(username);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con ese username.");
    }

    return inscripcionRepository.findByAlumno(alumnoOpt.get()).stream()
        .map(
            inscripcion ->
                new InscripcionDTO(
                    inscripcion.getCodigoDeInscripcion(),
                    inscripcion.getFecha(),
                    inscripcion.getHorario(),
                    inscripcion.getCurso().getCodigo(),
                    inscripcion.getCuatrimestre().getCodigo(),
                    inscripcion.getAlumno().getId()))
        .toList();
  }

  public InscripcionDTO obtenerInscripcionPorCodigo(Long codigoDeInscripcion) {
    Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(codigoDeInscripcion);

    if (inscripcionOpt.isEmpty()) {
      throw new InscripcionNoExisteException("No existe una inscripción con ese código.");
    }

    Inscripcion inscripcion = inscripcionOpt.get();
    return new InscripcionDTO(
        inscripcion.getCodigoDeInscripcion(),
        inscripcion.getFecha(),
        inscripcion.getHorario(),
        inscripcion.getCurso().getCodigo(),
        inscripcion.getCuatrimestre().getCodigo(),
        inscripcion.getAlumno().getId());
  }

  private void validarPeriodoDeInscripcion(Cuatrimestre cuatrimestre) {
    LocalDate fechaActual = LocalDate.now();
    LocalDate fechaInicioInscripcion = cuatrimestre.getFechaInicioPeriodoDeInscripcion();
    LocalDate fechaFinInscripcion = cuatrimestre.getFechaFinPeriodoDeInscripcion();

    if (fechaActual.isBefore(fechaInicioInscripcion) || fechaActual.isAfter(fechaFinInscripcion)) {
      throw new PeriodoInscripcionInvalidoException(
          String.format(
              "Las inscripciones para este cuatrimestre están disponibles desde %s hasta %s.",
              fechaInicioInscripcion, fechaFinInscripcion));
    }
  }
}
