package com.upp.service;

import com.upp.dto.CuatrimestreDTO;
import com.upp.exception.CuatrimestreExisteException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.FechasInvalidasException;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.CursoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CuatrimestreService {
  private final CuatrimestreRepository cuatrimestreRepository;
  private final CursoRepository cursoRepository;

  public CuatrimestreService(
      CuatrimestreRepository cuatrimestreRepository, CursoRepository cursoRepository) {
    this.cuatrimestreRepository = cuatrimestreRepository;
    this.cursoRepository = cursoRepository;
  }

  public CuatrimestreDTO crearCuatrimestre(CuatrimestreDTO cuatrimestreDTO) {
    if (cuatrimestreRepository.existsById(cuatrimestreDTO.getCodigo())) {
      throw new CuatrimestreExisteException("Ya existe un cuatrimestre con ese código.");
    }

    validarFechas(cuatrimestreDTO);

    Cuatrimestre cuatrimestre =
        new Cuatrimestre(
            cuatrimestreDTO.getCodigo(),
            cuatrimestreDTO.getFechaDeInicioClases(),
            cuatrimestreDTO.getFechaDeFinClases(),
            cuatrimestreDTO.getFechaInicioPeriodoDeInscripcion(),
            cuatrimestreDTO.getFechaFinPeriodoDeInscripcion(),
            cuatrimestreDTO.getFechaInicioPeriodoIntegradores(),
            cuatrimestreDTO.getFechaFinPeriodoIntegradores());

    // Asignar cursos si vienen en el DTO
    if (cuatrimestreDTO.getCodigosCursos() != null
        && !cuatrimestreDTO.getCodigosCursos().isEmpty()) {
      List<Curso> cursos = cursoRepository.findByCodigoIn(cuatrimestreDTO.getCodigosCursos());
      for (Curso curso : cursos) {
        curso.getCuatrimestres().add(cuatrimestre);
        cuatrimestre.getCursos().add(curso);
      }
    }

    cuatrimestreRepository.save(cuatrimestre);

    return cuatrimestreDTO;
  }

  public CuatrimestreDTO modificarCuatrimestre(String codigo, CuatrimestreDTO cuatrimestreDTO) {
    Optional<Cuatrimestre> cuatrimestreOpt = cuatrimestreRepository.findByCodigo(codigo);

    if (cuatrimestreOpt.isEmpty()) {
      throw new CuatrimestreNoExisteException("No existe un cuatrimestre con ese código.");
    }

    validarFechas(cuatrimestreDTO);

    Cuatrimestre cuatrimestre = cuatrimestreOpt.get();
    cuatrimestre.setFechaDeInicioClases(cuatrimestreDTO.getFechaDeInicioClases());
    cuatrimestre.setFechaDeFinClases(cuatrimestreDTO.getFechaDeFinClases());
    cuatrimestre.setFechaInicioPeriodoDeInscripcion(
        cuatrimestreDTO.getFechaInicioPeriodoDeInscripcion());
    cuatrimestre.setFechaFinPeriodoDeInscripcion(cuatrimestreDTO.getFechaFinPeriodoDeInscripcion());
    cuatrimestre.setFechaInicioPeriodoIntegradores(
        cuatrimestreDTO.getFechaInicioPeriodoIntegradores());
    cuatrimestre.setFechaFinPeriodoIntegradores(cuatrimestreDTO.getFechaFinPeriodoIntegradores());

    // Actualizar cursos si vienen en el DTO
    if (cuatrimestreDTO.getCodigosCursos() != null) {
      // Limpiar relaciones existentes
      for (Curso curso : new ArrayList<>(cuatrimestre.getCursos())) {
        curso.getCuatrimestres().remove(cuatrimestre);
      }
      cuatrimestre.getCursos().clear();

      // Asignar nuevos cursos
      if (!cuatrimestreDTO.getCodigosCursos().isEmpty()) {
        List<Curso> cursos = cursoRepository.findByCodigoIn(cuatrimestreDTO.getCodigosCursos());
        for (Curso curso : cursos) {
          curso.getCuatrimestres().add(cuatrimestre);
          cuatrimestre.getCursos().add(curso);
        }
      }
    }

    cuatrimestreRepository.save(cuatrimestre);
    return cuatrimestreDTO;
  }

  public void eliminarCuatrimestre(String codigo) {
    Optional<Cuatrimestre> cuatrimestreOpt = cuatrimestreRepository.findByCodigo(codigo);

    if (cuatrimestreOpt.isEmpty()) {
      throw new CuatrimestreNoExisteException("No existe un cuatrimestre con ese código.");
    }

    Cuatrimestre cuatrimestre = cuatrimestreOpt.get();

    // Limpiar las relaciones bidireccionales con cursos
    if (cuatrimestre.getCursos() != null) {
      for (Curso curso : cuatrimestre.getCursos()) {
        curso.getCuatrimestres().remove(cuatrimestre);
        cursoRepository.save(curso);
      }
      cuatrimestre.getCursos().clear();
    }

    cuatrimestreRepository.delete(cuatrimestre);
  }

  public CuatrimestreDTO obtenerCuatrimestrePorCodigo(String codigo) {
    Optional<Cuatrimestre> cuatrimestreOpt = cuatrimestreRepository.findByCodigo(codigo);

    if (cuatrimestreOpt.isEmpty()) {
      throw new CuatrimestreNoExisteException("No existe un cuatrimestre con ese código.");
    }

    Cuatrimestre cuatrimestre = cuatrimestreOpt.get();

    List<String> codigosCursos =
        cuatrimestre.getCursos().stream().map(Curso::getCodigo).collect(Collectors.toList());

    return new CuatrimestreDTO(
        cuatrimestre.getCodigo(),
        cuatrimestre.getFechaDeInicioClases(),
        cuatrimestre.getFechaDeFinClases(),
        cuatrimestre.getFechaInicioPeriodoDeInscripcion(),
        cuatrimestre.getFechaFinPeriodoDeInscripcion(),
        cuatrimestre.getFechaInicioPeriodoIntegradores(),
        cuatrimestre.getFechaFinPeriodoIntegradores(),
        codigosCursos);
  }

  public List<CuatrimestreDTO> obtenerTodosLosCuatrimestres() {
    return cuatrimestreRepository.findAll().stream()
        .map(
            cuatrimestre -> {
              List<String> codigosCursos =
                  cuatrimestre.getCursos().stream()
                      .map(Curso::getCodigo)
                      .collect(Collectors.toList());
              return new CuatrimestreDTO(
                  cuatrimestre.getCodigo(),
                  cuatrimestre.getFechaDeInicioClases(),
                  cuatrimestre.getFechaDeFinClases(),
                  cuatrimestre.getFechaInicioPeriodoDeInscripcion(),
                  cuatrimestre.getFechaFinPeriodoDeInscripcion(),
                  cuatrimestre.getFechaInicioPeriodoIntegradores(),
                  cuatrimestre.getFechaFinPeriodoIntegradores(),
                  codigosCursos);
            })
        .toList();
  }

  private void validarFechas(CuatrimestreDTO cuatrimestreDTO) {
    // Validar que fecha de inicio de clases sea antes que fecha de fin de clases
    if (cuatrimestreDTO.getFechaDeInicioClases().isAfter(cuatrimestreDTO.getFechaDeFinClases())) {
      throw new FechasInvalidasException(
          "La fecha de inicio de clases debe ser anterior a la fecha de fin de clases.");
    }

    // Validar que fecha de inicio de inscripción sea antes que fecha de fin de inscripción
    if (cuatrimestreDTO
        .getFechaInicioPeriodoDeInscripcion()
        .isAfter(cuatrimestreDTO.getFechaFinPeriodoDeInscripcion())) {
      throw new FechasInvalidasException(
          "La fecha de inicio del período de inscripción debe ser anterior a la fecha de fin del período de inscripción.");
    }

    // Validar que fecha de inicio de integradores sea antes que fecha de fin de integradores
    if (cuatrimestreDTO
        .getFechaInicioPeriodoIntegradores()
        .isAfter(cuatrimestreDTO.getFechaFinPeriodoIntegradores())) {
      throw new FechasInvalidasException(
          "La fecha de inicio del período de integradores debe ser anterior a la fecha de fin del período de integradores.");
    }

    // Validar que el período de inscripción sea antes del período de clases
    if (cuatrimestreDTO
        .getFechaFinPeriodoDeInscripcion()
        .isAfter(cuatrimestreDTO.getFechaDeInicioClases())) {
      throw new FechasInvalidasException(
          "El período de inscripción debe finalizar antes del inicio de clases.");
    }

    // Validar que el período de integradores sea después del período de clases
    if (cuatrimestreDTO
        .getFechaInicioPeriodoIntegradores()
        .isBefore(cuatrimestreDTO.getFechaDeFinClases())) {
      throw new FechasInvalidasException(
          "El período de integradores debe comenzar después del fin de clases.");
    }
  }
}
