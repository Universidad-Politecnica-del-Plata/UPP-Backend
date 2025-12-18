package com.upp.service;

import com.upp.dto.ActaDTO;
import com.upp.dto.ActaRequestDTO;
import com.upp.dto.AlumnoInscriptoDTO;
import com.upp.dto.EstadoActaRequestDTO;
import com.upp.dto.NotaDTO;
import com.upp.dto.NotaRequestDTO;
import com.upp.dto.NotasMasivasRequestDTO;
import com.upp.exception.ActaCerradaException;
import com.upp.exception.ActaExisteException;
import com.upp.exception.ActaNoExisteException;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.AlumnoNoInscriptoException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.NotaInvalidaException;
import com.upp.exception.NotaNoExisteException;
import com.upp.model.Acta;
import com.upp.model.Alumno;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.EstadoActa;
import com.upp.model.Inscripcion;
import com.upp.model.Nota;
import com.upp.repository.ActaRepository;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.CursoRepository;
import com.upp.repository.InscripcionRepository;
import com.upp.repository.NotaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActaService {

  private final ActaRepository actaRepository;
  private final NotaRepository notaRepository;
  private final CursoRepository cursoRepository;
  private final AlumnoRepository alumnoRepository;
  private final InscripcionRepository inscripcionRepository;
  private final CuatrimestreRepository cuatrimestreRepository;

  public ActaService(
      ActaRepository actaRepository,
      NotaRepository notaRepository,
      CursoRepository cursoRepository,
      AlumnoRepository alumnoRepository,
      InscripcionRepository inscripcionRepository,
      CuatrimestreRepository cuatrimestreRepository) {
    this.actaRepository = actaRepository;
    this.notaRepository = notaRepository;
    this.cursoRepository = cursoRepository;
    this.alumnoRepository = alumnoRepository;
    this.inscripcionRepository = inscripcionRepository;
    this.cuatrimestreRepository = cuatrimestreRepository;
  }

  public ActaDTO crearActa(ActaRequestDTO actaRequestDTO) {
    Optional<Curso> cursoOpt = cursoRepository.findByCodigo(actaRequestDTO.getCodigoCurso());
    if (cursoOpt.isEmpty()) {
      throw new CursoNoExisteException("No existe un curso con ese código.");
    }

    Curso curso = cursoOpt.get();

    // Verificar si ya existe un acta abierta del mismo tipo para este curso
    boolean existeActaAbierta =
        actaRepository.existsByCursoAndTipoDeActaAndEstado(
            curso, actaRequestDTO.getTipoDeActa(), EstadoActa.ABIERTA);

    if (existeActaAbierta) {
      throw new ActaExisteException(
          "Ya existe un acta abierta de este tipo para el curso especificado.");
    }

    Acta acta = new Acta(actaRequestDTO.getTipoDeActa(), actaRequestDTO.getEstado(), curso);
    actaRepository.save(acta);

    return convertToDTO(acta);
  }

  public ActaDTO obtenerActaPorId(Long numeroCorrelativo) {
    Optional<Acta> actaOpt = actaRepository.findById(numeroCorrelativo);
    if (actaOpt.isEmpty()) {
      throw new ActaNoExisteException("No existe un acta con ese número correlativo.");
    }

    return convertToDTO(actaOpt.get());
  }

  public List<ActaDTO> obtenerActasPorCurso(String codigoCurso) {
    Optional<Curso> cursoOpt = cursoRepository.findByCodigo(codigoCurso);
    if (cursoOpt.isEmpty()) {
      throw new CursoNoExisteException("No existe un curso con ese código.");
    }

    List<Acta> actas = actaRepository.findByCurso(cursoOpt.get());
    return actas.stream().map(this::convertToDTO).toList();
  }

  public List<ActaDTO> obtenerTodasLasActas() {
    List<Acta> actas = actaRepository.findAll();
    return actas.stream().map(this::convertToDTO).toList();
  }

  public ActaDTO actualizarEstadoActa(
      Long numeroCorrelativo, EstadoActaRequestDTO estadoRequestDTO) {
    Optional<Acta> actaOpt = actaRepository.findById(numeroCorrelativo);
    if (actaOpt.isEmpty()) {
      throw new ActaNoExisteException("No existe un acta con ese número correlativo.");
    }

    Acta acta = actaOpt.get();
    acta.setEstado(estadoRequestDTO.getEstado());
    actaRepository.save(acta);

    return convertToDTO(acta);
  }

  public NotaDTO agregarNota(Long numeroCorrelativo, NotaRequestDTO notaRequestDTO) {
    // Validar que la nota sea aprobatoria (entre 4 y 10)
    if (notaRequestDTO.getValor() < 4 || notaRequestDTO.getValor() > 10) {
      throw new NotaInvalidaException("Solo se pueden cargar notas aprobatorias (entre 4 y 10).");
    }

    Optional<Acta> actaOpt = actaRepository.findById(numeroCorrelativo);
    if (actaOpt.isEmpty()) {
      throw new ActaNoExisteException("No existe un acta con ese número correlativo.");
    }

    Acta acta = actaOpt.get();
    if (acta.getEstado() == EstadoActa.CERRADA) {
      throw new ActaCerradaException("No se pueden agregar notas a un acta cerrada.");
    }

    Optional<Alumno> alumnoOpt = alumnoRepository.findById(notaRequestDTO.getAlumnoId());
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con ese ID.");
    }

    Alumno alumno = alumnoOpt.get();

    // Verificar que el alumno esté inscripto en el curso para el cuatrimestre actual
    List<Cuatrimestre> cuatrimestresActuales =
        cuatrimestreRepository.findCuatrimestresByFecha(LocalDate.now());
    if (cuatrimestresActuales.isEmpty()) {
      throw new CuatrimestreNoExisteException("No hay cuatrimestres activos.");
    }

    Cuatrimestre cuatrimestreActual = cuatrimestresActuales.get(0);
    if (!inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(
        alumno, acta.getCurso(), cuatrimestreActual)) {
      throw new AlumnoNoInscriptoException(
          "El alumno no está inscripto en este curso para el cuatrimestre actual.");
    }

    // Verificar si ya existe una nota para este alumno en esta acta
    Optional<Nota> notaExistente = notaRepository.findByActaAndAlumno(acta, alumno);
    Nota nota;

    if (notaExistente.isPresent()) {
      // Si ya existe una nota, actualizarla
      nota = notaExistente.get();
      nota.setValor(notaRequestDTO.getValor());
    } else {
      // Si no existe, crear una nueva nota
      nota = new Nota(notaRequestDTO.getValor(), alumno, acta);
    }

    notaRepository.save(nota);

    return convertNotaToDTO(nota);
  }

  public NotaDTO actualizarNota(Long notaId, NotaRequestDTO notaRequestDTO) {
    // Validar que la nota sea aprobatoria (entre 4 y 10)
    if (notaRequestDTO.getValor() < 4 || notaRequestDTO.getValor() > 10) {
      throw new NotaInvalidaException("Solo se pueden cargar notas aprobatorias (entre 4 y 10).");
    }

    Optional<Nota> notaOpt = notaRepository.findById(notaId);
    if (notaOpt.isEmpty()) {
      throw new NotaNoExisteException("No existe una nota con ese ID.");
    }

    Nota nota = notaOpt.get();
    if (nota.getActa().getEstado() == EstadoActa.CERRADA) {
      throw new ActaCerradaException("No se pueden modificar notas de un acta cerrada.");
    }

    nota.setValor(notaRequestDTO.getValor());
    notaRepository.save(nota);

    return convertNotaToDTO(nota);
  }

  public List<NotaDTO> obtenerNotasPorActa(Long numeroCorrelativo) {
    Optional<Acta> actaOpt = actaRepository.findById(numeroCorrelativo);
    if (actaOpt.isEmpty()) {
      throw new ActaNoExisteException("No existe un acta con ese número correlativo.");
    }

    List<Nota> notas = notaRepository.findByActa(actaOpt.get());
    return notas.stream().map(this::convertNotaToDTO).toList();
  }

  public void eliminarNota(Long notaId) {
    Optional<Nota> notaOpt = notaRepository.findById(notaId);
    if (notaOpt.isEmpty()) {
      throw new NotaNoExisteException("No existe una nota con ese ID.");
    }

    Nota nota = notaOpt.get();
    if (nota.getActa().getEstado() == EstadoActa.CERRADA) {
      throw new ActaCerradaException("No se pueden eliminar notas de un acta cerrada.");
    }

    notaRepository.delete(nota);
  }

  public List<NotaDTO> agregarNotasMasivas(
      Long numeroCorrelativo, NotasMasivasRequestDTO notasMasivasRequestDTO) {
    Optional<Acta> actaOpt = actaRepository.findById(numeroCorrelativo);
    if (actaOpt.isEmpty()) {
      throw new ActaNoExisteException("No existe un acta con ese número correlativo.");
    }

    Acta acta = actaOpt.get();
    if (acta.getEstado() == EstadoActa.CERRADA) {
      throw new ActaCerradaException("No se pueden agregar notas a un acta cerrada.");
    }

    // Obtener el cuatrimestre actual
    List<Cuatrimestre> cuatrimestresActuales =
        cuatrimestreRepository.findCuatrimestresByFecha(acta.getFechaDeCreacion().toLocalDate());
    if (cuatrimestresActuales.isEmpty()) {
      throw new CuatrimestreNoExisteException("No hay cuatrimestres activos.");
    }
    Cuatrimestre cuatrimestreActual = cuatrimestresActuales.get(0);

    List<NotaDTO> notasCreadas = new ArrayList<>();

    for (NotaRequestDTO notaRequestDTO : notasMasivasRequestDTO.getNotas()) {
      // Validar que la nota sea aprobatoria (entre 4 y 10)
      if (notaRequestDTO.getValor() < 4 || notaRequestDTO.getValor() > 10) {
        throw new NotaInvalidaException("Solo se pueden cargar notas aprobatorias (entre 4 y 10).");
      }

      Optional<Alumno> alumnoOpt = alumnoRepository.findById(notaRequestDTO.getAlumnoId());
      if (alumnoOpt.isEmpty()) {
        throw new AlumnoNoExisteException(
            "No existe un alumno con ID: " + notaRequestDTO.getAlumnoId());
      }

      Alumno alumno = alumnoOpt.get();

      // Verificar que el alumno esté inscripto en el curso para el cuatrimestre actual
      if (!inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(
          alumno, acta.getCurso(), cuatrimestreActual)) {
        throw new AlumnoNoInscriptoException(
            "El alumno "
                + alumno.getNombre()
                + " "
                + alumno.getApellido()
                + " no está inscripto en este curso para el cuatrimestre actual.");
      }

      // Verificar si ya existe una nota para este alumno en esta acta
      Optional<Nota> notaExistente = notaRepository.findByActaAndAlumno(acta, alumno);
      Nota nota;

      if (notaExistente.isPresent()) {
        // Si ya existe una nota, actualizarla
        nota = notaExistente.get();
        nota.setValor(notaRequestDTO.getValor());
      } else {
        // Si no existe, crear una nueva nota
        nota = new Nota(notaRequestDTO.getValor(), alumno, acta);
      }

      notaRepository.save(nota);
      notasCreadas.add(convertNotaToDTO(nota));
    }

    return notasCreadas;
  }

  public List<AlumnoInscriptoDTO> obtenerAlumnosInscriptosPorActa(Long numeroCorrelativo) {
    Optional<Acta> actaOpt = actaRepository.findById(numeroCorrelativo);
    if (actaOpt.isEmpty()) {
      throw new ActaNoExisteException("No existe un acta con ese número correlativo.");
    }

    Acta acta = actaOpt.get();
    Curso curso = acta.getCurso();

    // Obtener el cuatrimestre que estaba activo cuando se creó el acta
    List<Cuatrimestre> cuatrimestresActivos =
        cuatrimestreRepository.findCuatrimestresByFecha(acta.getFechaDeCreacion().toLocalDate());
    if (cuatrimestresActivos.isEmpty()) {
      throw new CuatrimestreNoExisteException(
          "No había cuatrimestres activos en la fecha de creación del acta.");
    }

    Cuatrimestre cuatrimestreDelActa = cuatrimestresActivos.get(0);

    // Obtener las inscripciones para ese curso y cuatrimestre
    List<Inscripcion> inscripciones =
        inscripcionRepository.findByCursoAndCuatrimestre(curso, cuatrimestreDelActa);

    return inscripciones.stream()
        .map(
            inscripcion ->
                new AlumnoInscriptoDTO(
                    inscripcion.getAlumno().getId(),
                    inscripcion.getAlumno().getNombre(),
                    inscripcion.getAlumno().getApellido(),
                    inscripcion.getAlumno().getMatricula(),
                    inscripcion.getAlumno().getEmail()))
        .toList();
  }

  private ActaDTO convertToDTO(Acta acta) {
    List<NotaDTO> notasDTO = acta.getNotas().stream().map(this::convertNotaToDTO).toList();

    return new ActaDTO(
        acta.getNumeroCorrelativo(),
        acta.getTipoDeActa(),
        acta.getFechaDeCreacion(),
        acta.getEstado(),
        acta.getCurso().getCodigo(),
        notasDTO);
  }

  private NotaDTO convertNotaToDTO(Nota nota) {
    return new NotaDTO(
        nota.getId(),
        nota.getValor(),
        nota.getAlumno().getId(),
        nota.getAlumno().getNombre(),
        nota.getAlumno().getApellido(),
        nota.getAlumno().getMatricula(),
        nota.getActa().getNumeroCorrelativo());
  }
}
