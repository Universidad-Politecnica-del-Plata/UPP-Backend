package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.upp.dto.InscripcionDTO;
import com.upp.dto.InscripcionRequestDTO;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.InscripcionExisteException;
import com.upp.exception.InscripcionNoExisteException;
import com.upp.exception.PeriodoInscripcionInvalidoException;
import com.upp.model.Alumno;
import com.upp.model.Curso;
import com.upp.model.Cuatrimestre;
import com.upp.model.Inscripcion;
import com.upp.model.Materia;
import com.upp.model.TipoMateria;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.CursoRepository;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.InscripcionRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

  @Mock private InscripcionRepository inscripcionRepository;
  @Mock private AlumnoRepository alumnoRepository;
  @Mock private CursoRepository cursoRepository;
  @Mock private CuatrimestreRepository cuatrimestreRepository;

  @InjectMocks private InscripcionService inscripcionService;

  private InscripcionRequestDTO inscripcionRequestDTO;
  private Inscripcion inscripcion;
  private Alumno alumno;
  private Curso curso;
  private Cuatrimestre cuatrimestre;
  private Materia materia;

  @BeforeEach
  void setUp() {
    materia = new Materia(
        "123-M",
        "Análisis I",
        "Funciones y límites",
        8,
        0,
        TipoMateria.OBLIGATORIA);

    curso = new Curso("CURSO-001", 30, materia);

    cuatrimestre = new Cuatrimestre(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(3024, 7, 15),
        LocalDate.of(2024, 2, 1),
        LocalDate.of(3024, 3, 1),
        LocalDate.of(3024, 7, 16),
        LocalDate.of(3024, 8, 15));

    alumno = new Alumno();
    alumno.setId(1L);
    alumno.setMatricula(100001L);
    alumno.setNombre("Juan");
    alumno.setApellido("Pérez");
    alumno.setUsername("jperez");
    alumno.setPassword("password123");
    alumno.setDni(12345678L);
    alumno.setEmail("juan.perez@example.com");
    alumno.setTelefonos(new ArrayList<>());
    alumno.setCarreras(new ArrayList<>());
    alumno.setPlanesDeEstudio(new ArrayList<>());

    inscripcionRequestDTO = new InscripcionRequestDTO(
        "CURSO-001",
        "2024-1");

    inscripcion = new Inscripcion(curso, cuatrimestre, alumno);
    inscripcion.setCodigoDeInscripcion(1L);
  }

  @Test
  void crearInscripcionExitoso() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(false);
    when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(invocation -> {
      Inscripcion savedInscripcion = invocation.getArgument(0);
      savedInscripcion.setCodigoDeInscripcion(1L);
      return savedInscripcion;
    });

    InscripcionDTO resultado = inscripcionService.crearInscripcion(inscripcionRequestDTO, "jperez");

    assertNotNull(resultado);
    assertEquals(1L, resultado.getCodigoDeInscripcion());
    assertEquals("CURSO-001", resultado.getCodigoCurso());
    assertEquals("2024-1", resultado.getCodigoCuatrimestre());
    assertEquals(1L, resultado.getAlumnoId());
    assertNotNull(resultado.getFecha());
    assertNotNull(resultado.getHorario());

    verify(inscripcionRepository).save(any(Inscripcion.class));
  }

  @Test
  void crearInscripcionAlumnoNoExisteLanzaExcepcion() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.empty());

    assertThrows(AlumnoNoExisteException.class, () ->
        inscripcionService.crearInscripcion(inscripcionRequestDTO, "jperez"));

    verify(inscripcionRepository, never()).save(any(Inscripcion.class));
  }

  @Test
  void crearInscripcionCursoNoExisteLanzaExcepcion() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.empty());

    assertThrows(CursoNoExisteException.class, () ->
        inscripcionService.crearInscripcion(inscripcionRequestDTO, "jperez"));

    verify(inscripcionRepository, never()).save(any(Inscripcion.class));
  }

  @Test
  void crearInscripcionCuatrimestreNoExisteLanzaExcepcion() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.empty());

    assertThrows(CuatrimestreNoExisteException.class, () ->
        inscripcionService.crearInscripcion(inscripcionRequestDTO, "jperez"));

    verify(inscripcionRepository, never()).save(any(Inscripcion.class));
  }

  @Test
  void crearInscripcionYaExisteLanzaExcepcion() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(true);

    assertThrows(InscripcionExisteException.class, () ->
        inscripcionService.crearInscripcion(inscripcionRequestDTO, "jperez"));

    verify(inscripcionRepository, never()).save(any(Inscripcion.class));
  }

  @Test
  void eliminarInscripcionExitoso() {
    when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

    assertDoesNotThrow(() -> inscripcionService.eliminarInscripcion(1L));

    verify(inscripcionRepository).delete(inscripcion);
  }

  @Test
  void eliminarInscripcionNoExisteLanzaExcepcion() {
    when(inscripcionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(InscripcionNoExisteException.class, () ->
        inscripcionService.eliminarInscripcion(1L));

    verify(inscripcionRepository, never()).delete(any(Inscripcion.class));
  }

  @Test
  void obtenerInscripcionesPorUsernameExitoso() {
    List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(inscripcionRepository.findByAlumno(alumno)).thenReturn(inscripciones);

    List<InscripcionDTO> resultado = inscripcionService.obtenerInscripcionesPorUsername("jperez");

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals(1L, resultado.get(0).getCodigoDeInscripcion());
    assertEquals("CURSO-001", resultado.get(0).getCodigoCurso());
    assertEquals("2024-1", resultado.get(0).getCodigoCuatrimestre());
    assertEquals(1L, resultado.get(0).getAlumnoId());
  }

  @Test
  void obtenerInscripcionesPorUsernameAlumnoNoExisteLanzaExcepcion() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.empty());

    assertThrows(AlumnoNoExisteException.class, () ->
        inscripcionService.obtenerInscripcionesPorUsername("jperez"));

    verify(inscripcionRepository, never()).findByAlumno(any(Alumno.class));
  }

  @Test
  void obtenerInscripcionPorCodigoExitoso() {
    when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

    InscripcionDTO resultado = inscripcionService.obtenerInscripcionPorCodigo(1L);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getCodigoDeInscripcion());
    assertEquals("CURSO-001", resultado.getCodigoCurso());
    assertEquals("2024-1", resultado.getCodigoCuatrimestre());
    assertEquals(1L, resultado.getAlumnoId());
    assertNotNull(resultado.getFecha());
    assertNotNull(resultado.getHorario());
  }

  @Test
  void obtenerInscripcionPorCodigoNoExisteLanzaExcepcion() {
    when(inscripcionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(InscripcionNoExisteException.class, () ->
        inscripcionService.obtenerInscripcionPorCodigo(1L));
  }

  @Test
  void obtenerInscripcionesPorUsernameListaVacia() {
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(inscripcionRepository.findByAlumno(alumno)).thenReturn(new ArrayList<>());

    List<InscripcionDTO> resultado = inscripcionService.obtenerInscripcionesPorUsername("jperez");

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }

  @Test
  void crearInscripcionFueraDePeriodoTempranoLanzaExcepcion() {
    Cuatrimestre cuatrimestreFuturo = new Cuatrimestre(
        "2024-2",
        LocalDate.of(2024, 8, 1),
        LocalDate.of(2024, 12, 15),
        LocalDate.now().plusDays(30),  // Inicio en el futuro
        LocalDate.now().plusDays(60),  // Fin en el futuro
        LocalDate.of(2024, 12, 16),
        LocalDate.of(2025, 1, 15));

    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(cuatrimestreRepository.findByCodigo("2024-2")).thenReturn(Optional.of(cuatrimestreFuturo));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestreFuturo))
        .thenReturn(false);

    InscripcionRequestDTO inscripcionFutura = new InscripcionRequestDTO(
        "CURSO-001", "2024-2");

    assertThrows(PeriodoInscripcionInvalidoException.class, () ->
        inscripcionService.crearInscripcion(inscripcionFutura, "jperez"));

    verify(inscripcionRepository, never()).save(any(Inscripcion.class));
  }

  @Test
  void crearInscripcionFueraDePeriodoTardeLanzaExcepcion() {
    Cuatrimestre cuatrimestrePasado = new Cuatrimestre(
        "2023-2",
        LocalDate.of(2023, 8, 1),
        LocalDate.of(2023, 12, 15),
        LocalDate.now().minusDays(60),  // Inicio en el pasado
        LocalDate.now().minusDays(30),  // Fin en el pasado
        LocalDate.of(2023, 12, 16),
        LocalDate.of(2024, 1, 15));

    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(cuatrimestreRepository.findByCodigo("2023-2")).thenReturn(Optional.of(cuatrimestrePasado));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestrePasado))
        .thenReturn(false);

    InscripcionRequestDTO inscripcionPasada = new InscripcionRequestDTO(
        "CURSO-001", "2023-2");

    assertThrows(PeriodoInscripcionInvalidoException.class, () ->
        inscripcionService.crearInscripcion(inscripcionPasada, "jperez"));

    verify(inscripcionRepository, never()).save(any(Inscripcion.class));
  }

  @Test
  void crearInscripcionDentroDePeriodoExitoso() {
    // El cuatrimestre ya está configurado con período válido en setUp()
    when(alumnoRepository.findByUsername("jperez")).thenReturn(Optional.of(alumno));
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(alumno, curso, cuatrimestre))
        .thenReturn(false);
    when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

    InscripcionDTO resultado = inscripcionService.crearInscripcion(inscripcionRequestDTO, "jperez");

    assertNotNull(resultado);
    assertEquals("CURSO-001", resultado.getCodigoCurso());
    assertEquals("2024-1", resultado.getCodigoCuatrimestre());
    verify(inscripcionRepository).save(any(Inscripcion.class));
  }
}