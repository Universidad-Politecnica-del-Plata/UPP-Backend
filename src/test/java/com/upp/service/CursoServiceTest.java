package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.upp.dto.CursoDTO;
import com.upp.exception.CursoExisteException;
import com.upp.exception.CursoNoExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.model.Curso;
import com.upp.model.Materia;
import com.upp.model.Cuatrimestre;
import com.upp.model.TipoMateria;
import com.upp.repository.CursoRepository;
import com.upp.repository.MateriaRepository;
import com.upp.repository.CuatrimestreRepository;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

  @Mock private CursoRepository cursoRepository;

  @Mock private MateriaRepository materiaRepository;
  
  @Mock private CuatrimestreRepository cuatrimestreRepository;

  @InjectMocks private CursoService cursoService;

  private CursoDTO cursoDTO;
  private Curso curso;
  private Materia materia;
  private List<Cuatrimestre> cuatrimestres;
  private List<String> cuatrimestresId;

  @BeforeEach
  void setUp() {
    materia =
        new Materia(
            "123-M",
            "Análisis I",
            "Funciones y límites",
            8,
            0,
            TipoMateria.OBLIGATORIA);

    cuatrimestres = new ArrayList<>();

    cuatrimestresId = new ArrayList<>();

    cursoDTO = new CursoDTO("CURSO-001", 30, "123-M", cuatrimestresId);
    
    curso = new Curso("CURSO-001", 30, materia, cuatrimestres);
  }

  @Test
  void crearCursoExitoso() {
    when(cursoRepository.existsByCodigo("CURSO-001")).thenReturn(false);
    when(materiaRepository.findByCodigoDeMateria("123-M")).thenReturn(Optional.of(materia));
    when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

    CursoDTO resultado = cursoService.crearCurso(cursoDTO);

    assertNotNull(resultado);
    assertEquals("CURSO-001", resultado.getCodigo());
    assertEquals(30, resultado.getMaximoDeAlumnos());
    assertEquals("123-M", resultado.getCodigoMateria());
    assertNotNull(resultado.getCodigosCuatrimestres());
    assertTrue(resultado.getCodigosCuatrimestres().isEmpty());
    verify(cursoRepository).save(any(Curso.class));
  }

  @Test
  void crearCursoYaExisteLanzaExcepcion() {
    when(cursoRepository.existsByCodigo("CURSO-001")).thenReturn(true);

    assertThrows(CursoExisteException.class, () -> cursoService.crearCurso(cursoDTO));

    verify(cursoRepository, never()).save(any(Curso.class));
  }

  @Test
  void crearCursoMateriaNoExisteLanzaExcepcion() {
    when(cursoRepository.existsByCodigo("CURSO-001")).thenReturn(false);
    when(materiaRepository.findByCodigoDeMateria("123-M")).thenReturn(Optional.empty());

    assertThrows(MateriaNoExisteException.class, () -> cursoService.crearCurso(cursoDTO));

    verify(cursoRepository, never()).save(any(Curso.class));
  }

  @Test
  void obtenerCursoPorCodigoExitoso() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));

    CursoDTO resultado = cursoService.obtenerCursoPorCodigo("CURSO-001");

    assertNotNull(resultado);
    assertEquals("CURSO-001", resultado.getCodigo());
    assertEquals(30, resultado.getMaximoDeAlumnos());
    assertEquals("123-M", resultado.getCodigoMateria());
    assertNotNull(resultado.getCodigosCuatrimestres());
  }

  @Test
  void obtenerCursoPorCodigoNoExisteLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.empty());

    assertThrows(
        CursoNoExisteException.class, () -> cursoService.obtenerCursoPorCodigo("CURSO-001"));
  }

  @Test
  void obtenerTodosLosCursosExitoso() {
    Materia materia2 =
        new Materia(
            "124-M",
            "Análisis II",
            "Derivadas e integrales",
            8,
            8,
            TipoMateria.OBLIGATORIA);
    Curso curso2 = new Curso("CURSO-002", 25, materia2, new ArrayList<>());

    when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso, curso2));

    List<CursoDTO> resultado = cursoService.obtenerTodosLosCursos();

    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    assertEquals("CURSO-001", resultado.get(0).getCodigo());
    assertEquals("CURSO-002", resultado.get(1).getCodigo());
    assertNotNull(resultado.get(0).getCodigosCuatrimestres());
    assertNotNull(resultado.get(1).getCodigosCuatrimestres());
  }

  @Test
  void modificarCursoExitoso() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(materiaRepository.findByCodigoDeMateria("123-M")).thenReturn(Optional.of(materia));
    when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

    cursoDTO.setMaximoDeAlumnos(35);
    CursoDTO resultado = cursoService.modificarCurso("CURSO-001", cursoDTO);

    assertNotNull(resultado);
    assertEquals(35, resultado.getMaximoDeAlumnos());
    verify(cursoRepository).save(any(Curso.class));
  }

  @Test
  void modificarCursoNoExisteLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.empty());

    assertThrows(
        CursoNoExisteException.class, () -> cursoService.modificarCurso("CURSO-001", cursoDTO));

    verify(cursoRepository, never()).save(any(Curso.class));
  }

  @Test
  void modificarCursoMateriaNoExisteLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));
    when(materiaRepository.findByCodigoDeMateria("123-M")).thenReturn(Optional.empty());

    assertThrows(
        MateriaNoExisteException.class, () -> cursoService.modificarCurso("CURSO-001", cursoDTO));

    verify(cursoRepository, never()).save(any(Curso.class));
  }

  @Test
  void eliminarCursoExitoso() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.of(curso));

    assertDoesNotThrow(() -> cursoService.eliminarCurso("CURSO-001"));

    verify(cursoRepository).delete(curso);
  }

  @Test
  void eliminarCursoNoExisteLanzaExcepcion() {
    when(cursoRepository.findByCodigo("CURSO-001")).thenReturn(Optional.empty());

    assertThrows(CursoNoExisteException.class, () -> cursoService.eliminarCurso("CURSO-001"));

    verify(cursoRepository, never()).delete(any(Curso.class));
  }

  @Test
  void obtenerCursosPorMateriaExitoso() {
    when(materiaRepository.findByCodigoDeMateria("123-M")).thenReturn(Optional.of(materia));
    when(cursoRepository.findByMateria(materia)).thenReturn(Arrays.asList(curso));

    List<CursoDTO> resultado = cursoService.obtenerCursosPorMateria("123-M");

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals("CURSO-001", resultado.get(0).getCodigo());
    assertEquals("123-M", resultado.get(0).getCodigoMateria());
    assertNotNull(resultado.get(0).getCodigosCuatrimestres());
  }

  @Test
  void obtenerCursosPorMateriaMateriaNoExisteLanzaExcepcion() {
    when(materiaRepository.findByCodigoDeMateria("123-M")).thenReturn(Optional.empty());

    assertThrows(
        MateriaNoExisteException.class, () -> cursoService.obtenerCursosPorMateria("123-M"));

    verify(cursoRepository, never()).findByMateria(any(Materia.class));
  }
}