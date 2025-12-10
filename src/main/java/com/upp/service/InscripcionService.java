package com.upp.service;

import com.upp.dto.InscripcionDTO;
import com.upp.dto.InscripcionRequestDTO;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.CorrelativasNoAprobadasException;
import com.upp.exception.CreditosInsuficientesException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.InscripcionExisteException;
import com.upp.exception.InscripcionNoExisteException;
import com.upp.exception.PeriodoInscripcionInvalidoException;
import com.upp.model.Alumno;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.Inscripcion;
import com.upp.model.Materia;
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
  private final HistoriaAcademicaService historiaAcademicaService;

  public InscripcionService(
      InscripcionRepository inscripcionRepository,
      AlumnoRepository alumnoRepository,
      CursoRepository cursoRepository,
      CuatrimestreRepository cuatrimestreRepository,
      HistoriaAcademicaService historiaAcademicaService) {
    this.inscripcionRepository = inscripcionRepository;
    this.alumnoRepository = alumnoRepository;
    this.cursoRepository = cursoRepository;
    this.cuatrimestreRepository = cuatrimestreRepository;
    this.historiaAcademicaService = historiaAcademicaService;
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

    List<Cuatrimestre> cuatrimestreOpt =
        cuatrimestreRepository.findCuatrimestresByFecha(LocalDate.now());
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
    
    // Validaciones académicas: créditos y correlativas
    Materia materia = curso.getMateria();
    validarRequisitosAcademicos(alumno, materia);

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

  private void validarRequisitosAcademicos(Alumno alumno, Materia materia) {
    // Validar créditos suficientes
    Integer creditosAcumulados = historiaAcademicaService.calcularCreditosAcumulados(alumno);
    Integer creditosNecesarios = materia.getCreditosNecesarios();
    
    if (creditosAcumulados < creditosNecesarios) {
      throw new CreditosInsuficientesException(
          String.format("Créditos insuficientes. Tiene %d créditos, necesita %d para cursar %s.",
              creditosAcumulados, creditosNecesarios, materia.getNombre()));
    }

    // Validar correlativas aprobadas
    List<String> correlativasNoAprobadas = historiaAcademicaService.obtenerCorrelativasNoAprobadas(alumno, materia);
    
    if (!correlativasNoAprobadas.isEmpty()) {
      throw new CorrelativasNoAprobadasException(
          String.format("No se puede inscribir a %s. Correlativas no aprobadas: %s",
              materia.getNombre(), String.join(", ", correlativasNoAprobadas)));
    }
  }
}
