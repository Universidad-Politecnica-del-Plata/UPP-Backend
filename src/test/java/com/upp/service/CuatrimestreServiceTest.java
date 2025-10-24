package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.upp.dto.CuatrimestreDTO;
import com.upp.exception.CuatrimestreExisteException;
import com.upp.exception.CuatrimestreNoExisteException;
import com.upp.exception.FechasInvalidasException;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.repository.CuatrimestreRepository;
import com.upp.repository.CursoRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CuatrimestreServiceTest {

  @Mock private CuatrimestreRepository cuatrimestreRepository;

  @Mock private CursoRepository cursoRepository;

  @InjectMocks private CuatrimestreService cuatrimestreService;

  private CuatrimestreDTO cuatrimestreDTO;
  private Cuatrimestre cuatrimestre;

  @BeforeEach
  void setUp() {
    cuatrimestreDTO = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 28),
        LocalDate.of(2024, 7, 16),
        LocalDate.of(2024, 7, 31));

    cuatrimestre = new Cuatrimestre(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 28),
        LocalDate.of(2024, 7, 16),
        LocalDate.of(2024, 7, 31));
  }

  @Test
  void crearCuatrimestreExitoso() {
    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(false);
    when(cuatrimestreRepository.save(any(Cuatrimestre.class))).thenReturn(cuatrimestre);

    CuatrimestreDTO resultado = cuatrimestreService.crearCuatrimestre(cuatrimestreDTO);

    assertNotNull(resultado);
    assertEquals("2024-1", resultado.getCodigo());
    assertEquals(LocalDate.of(2024, 3, 1), resultado.getFechaDeInicioClases());
    assertEquals(LocalDate.of(2024, 7, 15), resultado.getFechaDeFinClases());
    assertEquals(LocalDate.of(2024, 2, 1), resultado.getFechaInicioPeriodoDeInscripcion());
    assertEquals(LocalDate.of(2024, 2, 28), resultado.getFechaFinPeriodoDeInscripcion());
    assertEquals(LocalDate.of(2024, 7, 16), resultado.getFechaInicioPeriodoIntegradores());
    assertEquals(LocalDate.of(2024, 7, 31), resultado.getFechaFinPeriodoIntegradores());
    verify(cuatrimestreRepository).save(any(Cuatrimestre.class));
  }

  @Test
  void crearCuatrimestreYaExisteLanzaExcepcion() {
    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(true);

    assertThrows(CuatrimestreExisteException.class, () -> cuatrimestreService.crearCuatrimestre(cuatrimestreDTO));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void obtenerCuatrimestrePorCodigoExitoso() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));

    CuatrimestreDTO resultado = cuatrimestreService.obtenerCuatrimestrePorCodigo("2024-1");

    assertNotNull(resultado);
    assertEquals("2024-1", resultado.getCodigo());
    assertEquals(LocalDate.of(2024, 3, 1), resultado.getFechaDeInicioClases());
    assertEquals(LocalDate.of(2024, 7, 15), resultado.getFechaDeFinClases());
    assertNotNull(resultado.getCodigosCursos());
  }

  @Test
  void obtenerCuatrimestrePorCodigoNoExisteLanzaExcepcion() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.empty());

    assertThrows(
        CuatrimestreNoExisteException.class, () -> cuatrimestreService.obtenerCuatrimestrePorCodigo("2024-1"));
  }

  @Test
  void obtenerTodosLosCuatrimestresExitoso() {
    Cuatrimestre cuatrimestre2 = new Cuatrimestre(
        "2024-2",
        LocalDate.of(2024, 8, 1),
        LocalDate.of(2024, 12, 15),
        LocalDate.of(2024, 7, 1),
        LocalDate.of(2024, 7, 31),
        LocalDate.of(2024, 12, 16),
        LocalDate.of(2024, 12, 31));

    when(cuatrimestreRepository.findAll()).thenReturn(Arrays.asList(cuatrimestre, cuatrimestre2));

    List<CuatrimestreDTO> resultado = cuatrimestreService.obtenerTodosLosCuatrimestres();

    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    assertEquals("2024-1", resultado.get(0).getCodigo());
    assertEquals("2024-2", resultado.get(1).getCodigo());
    assertNotNull(resultado.get(0).getCodigosCursos());
    assertNotNull(resultado.get(1).getCodigosCursos());
  }

  @Test
  void modificarCuatrimestreExitoso() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));
    when(cuatrimestreRepository.save(any(Cuatrimestre.class))).thenReturn(cuatrimestre);

    cuatrimestreDTO.setFechaDeInicioClases(LocalDate.of(2024, 3, 15));
    CuatrimestreDTO resultado = cuatrimestreService.modificarCuatrimestre("2024-1", cuatrimestreDTO);

    assertNotNull(resultado);
    assertEquals(LocalDate.of(2024, 3, 15), resultado.getFechaDeInicioClases());
    verify(cuatrimestreRepository).save(any(Cuatrimestre.class));
  }

  @Test
  void modificarCuatrimestreNoExisteLanzaExcepcion() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.empty());

    assertThrows(
        CuatrimestreNoExisteException.class, () -> cuatrimestreService.modificarCuatrimestre("2024-1", cuatrimestreDTO));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void eliminarCuatrimestreExitoso() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));

    assertDoesNotThrow(() -> cuatrimestreService.eliminarCuatrimestre("2024-1"));

    verify(cuatrimestreRepository).delete(cuatrimestre);
  }

  @Test
  void eliminarCuatrimestreNoExisteLanzaExcepcion() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.empty());

    assertThrows(CuatrimestreNoExisteException.class, () -> cuatrimestreService.eliminarCuatrimestre("2024-1"));

    verify(cuatrimestreRepository, never()).delete(any(Cuatrimestre.class));
  }

  @Test
  void obtenerCuatrimestreConCursosExitoso() {
    // Setup de cursos asociados
    Curso curso1 = new Curso();
    curso1.setCodigo("CURSO-001");
    Curso curso2 = new Curso();
    curso2.setCodigo("CURSO-002");
    
    List<Curso> cursos = Arrays.asList(curso1, curso2);
    cuatrimestre.setCursos(cursos);

    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));

    CuatrimestreDTO resultado = cuatrimestreService.obtenerCuatrimestrePorCodigo("2024-1");

    assertNotNull(resultado);
    assertEquals("2024-1", resultado.getCodigo());
    assertNotNull(resultado.getCodigosCursos());
    assertEquals(2, resultado.getCodigosCursos().size());
    assertTrue(resultado.getCodigosCursos().contains("CURSO-001"));
    assertTrue(resultado.getCodigosCursos().contains("CURSO-002"));
  }

  @Test
  void obtenerCuatrimestreSinCursosExitoso() {
    cuatrimestre.setCursos(new ArrayList<>());
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));

    CuatrimestreDTO resultado = cuatrimestreService.obtenerCuatrimestrePorCodigo("2024-1");

    assertNotNull(resultado);
    assertEquals("2024-1", resultado.getCodigo());
    assertNotNull(resultado.getCodigosCursos());
    assertTrue(resultado.getCodigosCursos().isEmpty());
  }

  @Test
  void crearCuatrimestre_FechaInicioClasesPosteriorAFinLanzaExcepcion() {
    CuatrimestreDTO cuatrimestreInvalido = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 7, 15), // Fecha de inicio POSTERIOR a fecha de fin
        LocalDate.of(2024, 3, 1),  // Fecha de fin ANTERIOR a fecha de inicio
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 28),
        LocalDate.of(2024, 7, 16),
        LocalDate.of(2024, 7, 31));

    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(false);

    assertThrows(FechasInvalidasException.class, () -> cuatrimestreService.crearCuatrimestre(cuatrimestreInvalido));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void crearCuatrimestre_FechaInicioInscripcionPosteriorAFinLanzaExcepcion() {
    CuatrimestreDTO cuatrimestreInvalido = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2024, 2, 28), // Fecha de inicio POSTERIOR a fecha de fin
        LocalDate.of(2024, 2, 1),  // Fecha de fin ANTERIOR a fecha de inicio
        LocalDate.of(2024, 7, 16),
        LocalDate.of(2024, 7, 31));

    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(false);

    assertThrows(FechasInvalidasException.class, () -> cuatrimestreService.crearCuatrimestre(cuatrimestreInvalido));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void crearCuatrimestreFechaInicioIntegradoresPosteriorAFinLanzaExcepcion() {
    CuatrimestreDTO cuatrimestreInvalido = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 28),
        LocalDate.of(2024, 7, 31), // Fecha de inicio POSTERIOR a fecha de fin
        LocalDate.of(2024, 7, 16)); // Fecha de fin ANTERIOR a fecha de inicio

    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(false);

    assertThrows(FechasInvalidasException.class, () -> cuatrimestreService.crearCuatrimestre(cuatrimestreInvalido));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void crearCuatrimestreInscripcionFinalizaDespuesDeInicioClasesLanzaExcepcion() {
    CuatrimestreDTO cuatrimestreInvalido = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 3, 15), // Fecha de fin de inscripción POSTERIOR al inicio de clases
        LocalDate.of(2024, 7, 16),
        LocalDate.of(2024, 7, 31));

    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(false);

    assertThrows(FechasInvalidasException.class, () -> cuatrimestreService.crearCuatrimestre(cuatrimestreInvalido));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void crearCuatrimestreIntegradoresComenzanAntesDeFinClasesLanzaExcepcion() {
    CuatrimestreDTO cuatrimestreInvalido = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 28),
        LocalDate.of(2024, 7, 10), // Fecha de inicio de integradores ANTERIOR al fin de clases
        LocalDate.of(2024, 7, 31));

    when(cuatrimestreRepository.existsById("2024-1")).thenReturn(false);

    assertThrows(FechasInvalidasException.class, () -> cuatrimestreService.crearCuatrimestre(cuatrimestreInvalido));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }

  @Test
  void modificarCuatrimestreFechasInvalidasLanzaExcepcion() {
    when(cuatrimestreRepository.findByCodigo("2024-1")).thenReturn(Optional.of(cuatrimestre));

    CuatrimestreDTO cuatrimestreInvalido = new CuatrimestreDTO(
        "2024-1",
        LocalDate.of(2024, 7, 15), // Fecha de inicio POSTERIOR a fecha de fin
        LocalDate.of(2024, 3, 1),  // Fecha de fin ANTERIOR a fecha de inicio
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 28),
        LocalDate.of(2024, 7, 16),
        LocalDate.of(2024, 7, 31));

    assertThrows(FechasInvalidasException.class, () -> cuatrimestreService.modificarCuatrimestre("2024-1", cuatrimestreInvalido));

    verify(cuatrimestreRepository, never()).save(any(Cuatrimestre.class));
  }
}