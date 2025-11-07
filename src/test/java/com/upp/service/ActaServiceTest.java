package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.upp.dto.ActaDTO;
import com.upp.dto.ActaRequestDTO;
import com.upp.dto.EstadoActaRequestDTO;
import com.upp.dto.NotaDTO;
import com.upp.dto.NotaRequestDTO;
import com.upp.exception.ActaCerradaException;
import com.upp.exception.ActaExisteException;
import com.upp.exception.ActaNoExisteException;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.AlumnoNoInscriptoException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.InscripcionExisteException;
import com.upp.exception.NotaInvalidaException;
import com.upp.exception.NotaNoExisteException;
import com.upp.model.Acta;
import com.upp.model.Alumno;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.EstadoActa;
import com.upp.model.Materia;
import com.upp.model.Nota;
import com.upp.model.TipoDeActa;
import com.upp.model.TipoMateria;
import com.upp.repository.ActaRepository;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.CursoRepository;
import com.upp.repository.InscripcionRepository;
import com.upp.repository.NotaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActaServiceTest {

  @Mock private ActaRepository actaRepository;
  @Mock private NotaRepository notaRepository;
  @Mock private CursoRepository cursoRepository;
  @Mock private AlumnoRepository alumnoRepository;
  @Mock private InscripcionRepository inscripcionRepository;
  @Mock private CuatrimestreRepository cuatrimestreRepository;

  @InjectMocks private ActaService actaService;

  private ActaRequestDTO actaRequestDTO;
  private EstadoActaRequestDTO estadoActaRequestDTO;
  private NotaRequestDTO notaRequestDTO;
  private Acta acta;
  private Nota nota;
  private Alumno alumno;
  private Curso curso;
  private Materia materia;
  private Cuatrimestre cuatrimestre;

  @BeforeEach
  void setUp() {
    materia =
        new Materia("123-M", "Análisis I", "Funciones y límites", 8, 0, TipoMateria.OBLIGATORIA);

    curso = new Curso("CURSO-001", 30, materia);

    cuatrimestre =
        new Cuatrimestre(
            "2024-1",
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 7, 15),
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 7, 16),
            LocalDate.of(2024, 8, 15));

    alumno = new Alumno();
    alumno.setId(1L);
    alumno.setMatricula(100001L);
    alumno.setNombre("Juan");
    alumno.setApellido("Pérez");
    alumno.setUsername("jperez");
    alumno.setPassword("password123");
    alumno.setDni(12345678L);
    alumno.setEmail("juan.perez@example.com");

    acta = new Acta(TipoDeActa.CURSADA, EstadoActa.ABIERTA, curso);
    acta.setNumeroCorrelativo(1L);
    acta.setFechaDeCreacion(LocalDateTime.now());

    nota = new Nota(8, alumno, acta);
    nota.setId(1L);

    actaRequestDTO = new ActaRequestDTO(TipoDeActa.CURSADA, EstadoActa.ABIERTA, "CURSO-001");
    estadoActaRequestDTO = new EstadoActaRequestDTO(EstadoActa.CERRADA);
    notaRequestDTO = new NotaRequestDTO(9, 1L);
  }

  @Test
  void crearActaExitoso() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(actaRepository.existsByCursoAndTipoDeActaAndEstado(
            curso, TipoDeActa.CURSADA, EstadoActa.ABIERTA))
        .thenReturn(false);
    when(actaRepository.save(any(Acta.class)))
        .thenAnswer(
            invocation -> {
              Acta savedActa = invocation.getArgument(0);
              savedActa.setNumeroCorrelativo(1L);
              return savedActa;
            });

    ActaDTO resultado = actaService.crearActa(actaRequestDTO);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getNumeroCorrelativo());
    assertEquals(TipoDeActa.CURSADA, resultado.getTipoDeActa());
    assertEquals(EstadoActa.ABIERTA, resultado.getEstado());
    assertEquals("CURSO-001", resultado.getCodigoCurso());
    assertNotNull(resultado.getFechaDeCreacion());
    assertNotNull(resultado.getNotas());

    verify(actaRepository).save(any(Acta.class));
  }

  @Test
  void crearActaCursoNoExisteLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.empty());

    assertThrows(CursoNoExisteException.class, () -> actaService.crearActa(actaRequestDTO));

    verify(actaRepository, never()).save(any(Acta.class));
  }

  @Test
  void crearActaYaExisteActaAbiertaLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(actaRepository.existsByCursoAndTipoDeActaAndEstado(
            curso, TipoDeActa.CURSADA, EstadoActa.ABIERTA))
        .thenReturn(true);

    assertThrows(ActaExisteException.class, () -> actaService.crearActa(actaRequestDTO));

    verify(actaRepository, never()).save(any(Acta.class));
  }

  @Test
  void obtenerActaPorIdExitoso() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));

    ActaDTO resultado = actaService.obtenerActaPorId(1L);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getNumeroCorrelativo());
    assertEquals(TipoDeActa.CURSADA, resultado.getTipoDeActa());
    assertEquals(EstadoActa.ABIERTA, resultado.getEstado());
    assertEquals("CURSO-001", resultado.getCodigoCurso());
  }

  @Test
  void obtenerActaPorIdNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ActaNoExisteException.class, () -> actaService.obtenerActaPorId(1L));
  }

  @Test
  void obtenerActasPorCursoExitoso() {
    List<Acta> actas = List.of(acta);
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(actaRepository.findByCurso(curso)).thenReturn(actas);

    List<ActaDTO> resultado = actaService.obtenerActasPorCurso("CURSO-001");

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals(1L, resultado.get(0).getNumeroCorrelativo());
    assertEquals("CURSO-001", resultado.get(0).getCodigoCurso());
  }

  @Test
  void obtenerActasPorCursoCursoNoExisteLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.empty());

    assertThrows(CursoNoExisteException.class, () -> actaService.obtenerActasPorCurso("CURSO-001"));
  }

  @Test
  void obtenerActasPorCursoListaVacia() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(actaRepository.findByCurso(curso)).thenReturn(new ArrayList<>());

    List<ActaDTO> resultado = actaService.obtenerActasPorCurso("CURSO-001");

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }

  @Test
  void actualizarEstadoActaExitoso() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(actaRepository.save(any(Acta.class))).thenReturn(acta);

    ActaDTO resultado = actaService.actualizarEstadoActa(1L, estadoActaRequestDTO);

    assertNotNull(resultado);
    assertEquals(EstadoActa.CERRADA, resultado.getEstado());
    verify(actaRepository).save(acta);
  }

  @Test
  void actualizarEstadoActaNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        ActaNoExisteException.class,
        () -> actaService.actualizarEstadoActa(1L, estadoActaRequestDTO));

    verify(actaRepository, never()).save(any(Acta.class));
  }

  @Test
  void agregarNotaExitoso() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(cuatrimestreRepository.findCuatrimestresActuales(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(true);
    when(notaRepository.existsByActaAndAlumno(acta, alumno)).thenReturn(false);
    when(notaRepository.save(any(Nota.class)))
        .thenAnswer(
            invocation -> {
              Nota savedNota = invocation.getArgument(0);
              savedNota.setId(1L);
              return savedNota;
            });

    NotaDTO resultado = actaService.agregarNota(1L, notaRequestDTO);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(9, resultado.getValor());
    assertEquals(1L, resultado.getAlumnoId());
    assertEquals("Juan", resultado.getNombreAlumno());
    assertEquals("Pérez", resultado.getApellidoAlumno());
    assertEquals(100001L, resultado.getMatriculaAlumno());
    assertEquals(1L, resultado.getNumeroCorrelativoActa());

    verify(notaRepository).save(any(Nota.class));
  }

  @Test
  void agregarNotaActaNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ActaNoExisteException.class, () -> actaService.agregarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaActaCerradaLanzaExcepcion() {
    acta.setEstado(EstadoActa.CERRADA);
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));

    assertThrows(ActaCerradaException.class, () -> actaService.agregarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaAlumnoNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AlumnoNoExisteException.class, () -> actaService.agregarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaYaExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(cuatrimestreRepository.findCuatrimestresActuales(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(true);
    when(notaRepository.existsByActaAndAlumno(acta, alumno)).thenReturn(true);

    assertThrows(
        InscripcionExisteException.class, () -> actaService.agregarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void actualizarNotaExitoso() {
    when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));
    when(notaRepository.save(any(Nota.class))).thenReturn(nota);

    NotaDTO resultado = actaService.actualizarNota(1L, notaRequestDTO);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(9, resultado.getValor());
    verify(notaRepository).save(nota);
  }

  @Test
  void actualizarNotaNoExisteLanzaExcepcion() {
    when(notaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotaNoExisteException.class, () -> actaService.actualizarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void actualizarNotaActaCerradaLanzaExcepcion() {
    acta.setEstado(EstadoActa.CERRADA);
    when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

    assertThrows(ActaCerradaException.class, () -> actaService.actualizarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void obtenerNotasPorActaExitoso() {
    List<Nota> notas = List.of(nota);
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(notaRepository.findByActa(acta)).thenReturn(notas);

    List<NotaDTO> resultado = actaService.obtenerNotasPorActa(1L);

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals(1L, resultado.get(0).getId());
    assertEquals(8, resultado.get(0).getValor());
    assertEquals(1L, resultado.get(0).getAlumnoId());
  }

  @Test
  void obtenerNotasPorActaNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ActaNoExisteException.class, () -> actaService.obtenerNotasPorActa(1L));
  }

  @Test
  void obtenerNotasPorActaListaVacia() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(notaRepository.findByActa(acta)).thenReturn(new ArrayList<>());

    List<NotaDTO> resultado = actaService.obtenerNotasPorActa(1L);

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }

  @Test
  void eliminarNotaExitoso() {
    when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

    assertDoesNotThrow(() -> actaService.eliminarNota(1L));

    verify(notaRepository).delete(nota);
  }

  @Test
  void eliminarNotaNoExisteLanzaExcepcion() {
    when(notaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotaNoExisteException.class, () -> actaService.eliminarNota(1L));

    verify(notaRepository, never()).delete(any(Nota.class));
  }

  @Test
  void eliminarNotaActaCerradaLanzaExcepcion() {
    acta.setEstado(EstadoActa.CERRADA);
    when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

    assertThrows(ActaCerradaException.class, () -> actaService.eliminarNota(1L));

    verify(notaRepository, never()).delete(any(Nota.class));
  }

  @Test
  void agregarNotaAlumnoNoInscriptoLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(cuatrimestreRepository.findCuatrimestresActuales(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(false);

    assertThrows(
        AlumnoNoInscriptoException.class, () -> actaService.agregarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaNoCuatrimestreActivoLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(cuatrimestreRepository.findCuatrimestresActuales(any(LocalDate.class)))
        .thenReturn(new ArrayList<>());

    assertThrows(
        CuatrimestreNoExisteException.class, () -> actaService.agregarNota(1L, notaRequestDTO));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaConGradoBajoLanzaExcepcion() {
    NotaRequestDTO notaInvalida = new NotaRequestDTO(3, 1L); // Nota menor a 4

    assertThrows(NotaInvalidaException.class, () -> actaService.agregarNota(1L, notaInvalida));

    verify(actaRepository, never()).findById(any());
    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaConGradoAltoLanzaExcepcion() {
    NotaRequestDTO notaInvalida = new NotaRequestDTO(11, 1L); // Nota mayor a 10

    assertThrows(NotaInvalidaException.class, () -> actaService.agregarNota(1L, notaInvalida));

    verify(actaRepository, never()).findById(any());
    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotaConGradoMinimoValidoExitoso() {
    NotaRequestDTO notaMinima = new NotaRequestDTO(4, 1L); // Nota mínima válida
    
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(cuatrimestreRepository.findCuatrimestresActuales(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(true);
    when(notaRepository.existsByActaAndAlumno(acta, alumno)).thenReturn(false);
    when(notaRepository.save(any(Nota.class)))
        .thenAnswer(
            invocation -> {
              Nota savedNota = invocation.getArgument(0);
              savedNota.setId(2L);
              return savedNota;
            });

    NotaDTO resultado = actaService.agregarNota(1L, notaMinima);

    assertNotNull(resultado);
    assertEquals(2L, resultado.getId());
    assertEquals(4, resultado.getValor());
    assertEquals(1L, resultado.getAlumnoId());

    verify(notaRepository).save(any(Nota.class));
  }

  @Test
  void agregarNotaConGradoMaximoValidoExitoso() {
    NotaRequestDTO notaMaxima = new NotaRequestDTO(10, 1L); // Nota máxima válida
    
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(cuatrimestreRepository.findCuatrimestresActuales(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(true);
    when(notaRepository.existsByActaAndAlumno(acta, alumno)).thenReturn(false);
    when(notaRepository.save(any(Nota.class)))
        .thenAnswer(
            invocation -> {
              Nota savedNota = invocation.getArgument(0);
              savedNota.setId(3L);
              return savedNota;
            });

    NotaDTO resultado = actaService.agregarNota(1L, notaMaxima);

    assertNotNull(resultado);
    assertEquals(3L, resultado.getId());
    assertEquals(10, resultado.getValor());
    assertEquals(1L, resultado.getAlumnoId());

    verify(notaRepository).save(any(Nota.class));
  }

  @Test
  void actualizarNotaConGradoBajoLanzaExcepcion() {
    NotaRequestDTO notaInvalida = new NotaRequestDTO(2, 1L); // Nota menor a 4

    assertThrows(NotaInvalidaException.class, () -> actaService.actualizarNota(1L, notaInvalida));

    verify(notaRepository, never()).findById(any());
    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void actualizarNotaConGradoAltoLanzaExcepcion() {
    NotaRequestDTO notaInvalida = new NotaRequestDTO(12, 1L); // Nota mayor a 10

    assertThrows(NotaInvalidaException.class, () -> actaService.actualizarNota(1L, notaInvalida));

    verify(notaRepository, never()).findById(any());
    verify(notaRepository, never()).save(any(Nota.class));
  }
}
