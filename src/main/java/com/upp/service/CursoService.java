package com.upp.service;

import com.upp.dto.CursoDTO;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.CursoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.Materia;
import com.upp.model.PlanDeEstudios;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.CursoRepository;
import com.upp.repository.MateriaRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CursoService {
  private final CursoRepository cursoRepository;
  private final MateriaRepository materiaRepository;
  private final CuatrimestreRepository cuatrimestreRepository;
  private final PlanDeEstudiosRepository planDeEstudiosRepository;

  public CursoService(
      CursoRepository cursoRepository,
      MateriaRepository materiaRepository,
      CuatrimestreRepository cuatrimestreRepository,
      PlanDeEstudiosRepository planDeEstudiosRepository) {
    this.cursoRepository = cursoRepository;
    this.materiaRepository = materiaRepository;
    this.cuatrimestreRepository = cuatrimestreRepository;
    this.planDeEstudiosRepository = planDeEstudiosRepository;
  }

  public CursoDTO crearCurso(CursoDTO cursoDTO) {
    if (cursoRepository.existsByCodigo(cursoDTO.getCodigo())) {
      throw new CursoExisteException("Ya existe un curso con ese código.");
    }

    Optional<Materia> materiaOpt =
        materiaRepository.findByCodigoDeMateria(cursoDTO.getCodigoMateria());
    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No existe una materia con ese código.");
    }

    List<Cuatrimestre> cuatrimestres = new ArrayList<>();
    if (cursoDTO.getCodigosCuatrimestres() != null
        && !cursoDTO.getCodigosCuatrimestres().isEmpty()) {
      cuatrimestres = cuatrimestreRepository.findByCodigoIn(cursoDTO.getCodigosCuatrimestres());
    }

    if (cursoDTO.getCodigosCuatrimestres() != null
        && cuatrimestres.size() != cursoDTO.getCodigosCuatrimestres().size()) {
      throw new CuatrimestreNoExisteException("No existe un cuatrimestre con ese código.");
    }

    Curso curso =
        new Curso(
            cursoDTO.getCodigo(), cursoDTO.getMaximoDeAlumnos(), materiaOpt.get(), cuatrimestres);
    cursoRepository.save(curso);

    return cursoDTO;
  }

  public CursoDTO modificarCurso(String codigo, CursoDTO cursoDTO) {
    Optional<Curso> cursoOpt = cursoRepository.findByCodigo(codigo);

    if (cursoOpt.isEmpty()) {
      throw new CursoNoExisteException("No existe un curso con ese código.");
    }

    Curso curso = cursoOpt.get();
    curso.setMaximoDeAlumnos(cursoDTO.getMaximoDeAlumnos());

    List<Cuatrimestre> cuatrimestres = new ArrayList<>();
    if (cursoDTO.getCodigosCuatrimestres() != null
        && !cursoDTO.getCodigosCuatrimestres().isEmpty()) {
      cuatrimestres = cuatrimestreRepository.findByCodigoIn(cursoDTO.getCodigosCuatrimestres());
    }

    if (cursoDTO.getCodigosCuatrimestres() != null
        && cuatrimestres.size() != cursoDTO.getCodigosCuatrimestres().size()) {
      throw new CuatrimestreNoExisteException("No existe un cuatrimestre con ese código.");
    }

    curso.setCuatrimestres(cuatrimestres);

    if (cursoDTO.getCodigoMateria() != null) {
      Optional<Materia> materiaOpt =
          materiaRepository.findByCodigoDeMateria(cursoDTO.getCodigoMateria());
      if (materiaOpt.isEmpty()) {
        throw new MateriaNoExisteException("No existe una materia con ese código.");
      }
      curso.setMateria(materiaOpt.get());
    }

    cursoRepository.save(curso);
    return cursoDTO;
  }

  public void eliminarCurso(String codigo) {
    Optional<Curso> cursoOpt = cursoRepository.findByCodigo(codigo);

    if (cursoOpt.isEmpty()) {
      throw new CursoNoExisteException("No existe un curso con ese código.");
    }

    cursoRepository.delete(cursoOpt.get());
  }

  public CursoDTO obtenerCursoPorCodigo(String codigo) {
    Optional<Curso> cursoOpt = cursoRepository.findByCodigo(codigo);

    if (cursoOpt.isEmpty()) {
      throw new CursoNoExisteException("No existe un curso con ese código.");
    }

    Curso curso = cursoOpt.get();

    List<String> codigosCuatrimestres =
        curso.getCuatrimestres().stream().map(Cuatrimestre::getCodigo).collect(Collectors.toList());

    return new CursoDTO(
        curso.getCodigo(),
        curso.getMaximoDeAlumnos(),
        curso.getMateria().getCodigoDeMateria(),
        codigosCuatrimestres);
  }

  public List<CursoDTO> obtenerTodosLosCursos() {
    return cursoRepository.findAll().stream()
        .map(
            curso -> {
              List<String> codigosCuatrimestres =
                  curso.getCuatrimestres().stream()
                      .map(Cuatrimestre::getCodigo)
                      .collect(Collectors.toList());
              return new CursoDTO(
                  curso.getCodigo(),
                  curso.getMaximoDeAlumnos(),
                  curso.getMateria().getCodigoDeMateria(),
                  codigosCuatrimestres);
            })
        .toList();
  }

  public List<CursoDTO> obtenerCursosPorMateria(String codigoMateria) {
    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigoMateria);
    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No existe una materia con ese código.");
    }

    return cursoRepository.findByMateria(materiaOpt.get()).stream()
        .map(
            curso -> {
              List<String> codigosCuatrimestres =
                  curso.getCuatrimestres().stream()
                      .map(Cuatrimestre::getCodigo)
                      .collect(Collectors.toList());
              return new CursoDTO(
                  curso.getCodigo(),
                  curso.getMaximoDeAlumnos(),
                  curso.getMateria().getCodigoDeMateria(),
                  codigosCuatrimestres);
            })
        .toList();
  }

  public List<CursoDTO> obtenerCursosPorPlanDeEstudios(String codigoPlan) {
    Optional<PlanDeEstudios> planOpt =
        planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigoPlan);
    if (planOpt.isEmpty()) {
      throw new PlanDeEstudiosNoExisteException("No existe un plan de estudios con ese código.");
    }

    PlanDeEstudios plan = planOpt.get();
    List<String> codigosMaterias = plan.getCodigosMaterias();

    List<Materia> materias = materiaRepository.findByCodigoDeMateria_In(codigosMaterias);

    List<Cuatrimestre> cuatrimestresActuales =
        cuatrimestreRepository.findCuatrimestresByFecha(LocalDate.now());

    return materias.stream()
        .flatMap(materia -> cursoRepository.findByMateria(materia).stream())
        .filter(
            curso -> curso.getCuatrimestres().stream().anyMatch(cuatrimestresActuales::contains))
        .map(
            curso -> {
              List<String> codigosCuatrimestres =
                  curso.getCuatrimestres().stream()
                      .map(Cuatrimestre::getCodigo)
                      .collect(Collectors.toList());
              return new CursoDTO(
                  curso.getCodigo(),
                  curso.getMaximoDeAlumnos(),
                  curso.getMateria().getCodigoDeMateria(),
                  codigosCuatrimestres);
            })
        .toList();
  }
}
