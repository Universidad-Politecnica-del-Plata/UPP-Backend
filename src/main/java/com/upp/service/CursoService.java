package com.upp.service;

import com.upp.dto.CursoDTO;
import com.upp.exception.CursoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.model.Curso;
import com.upp.model.Materia;
import com.upp.repository.CursoRepository;
import com.upp.repository.MateriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CursoService {
  private final CursoRepository cursoRepository;
  private final MateriaRepository materiaRepository;

  public CursoService(CursoRepository cursoRepository, MateriaRepository materiaRepository) {
    this.cursoRepository = cursoRepository;
    this.materiaRepository = materiaRepository;
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

    Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getMaximoDeAlumnos(), materiaOpt.get());
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

    return new CursoDTO(
        curso.getCodigo(),
        curso.getMaximoDeAlumnos(),
        curso.getMateria().getCodigoDeMateria());
  }

  public List<CursoDTO> obtenerTodosLosCursos() {
    return cursoRepository.findAll().stream()
        .map(
            curso ->
                new CursoDTO(
                    curso.getCodigo(),
                    curso.getMaximoDeAlumnos(),
                    curso.getMateria().getCodigoDeMateria()))
        .toList();
  }

  public List<CursoDTO> obtenerCursosPorMateria(String codigoMateria) {
    Optional<Materia> materiaOpt = materiaRepository.findByCodigoDeMateria(codigoMateria);
    if (materiaOpt.isEmpty()) {
      throw new MateriaNoExisteException("No existe una materia con ese código.");
    }

    return cursoRepository.findByMateria(materiaOpt.get()).stream()
        .map(
            curso ->
                new CursoDTO(
                    curso.getCodigo(),
                    curso.getMaximoDeAlumnos(),
                    curso.getMateria().getCodigoDeMateria()))
        .toList();
  }
}