package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CuatrimestreTest {
  
  @Test
  void crearSinParametros() {
    new Cuatrimestre();
  }

  @Test
  void crearConParametros() {
    String codigo = "2024-1";
    LocalDate fechaInicioClases = LocalDate.of(2024, 3, 1);
    LocalDate fechaFinClases = LocalDate.of(2024, 7, 15);
    LocalDate fechaInicioInscripcion = LocalDate.of(2024, 2, 1);
    LocalDate fechaFinInscripcion = LocalDate.of(2024, 2, 28);
    LocalDate fechaInicioIntegradores = LocalDate.of(2024, 7, 16);
    LocalDate fechaFinIntegradores = LocalDate.of(2024, 7, 31);

    Cuatrimestre cuatrimestre = new Cuatrimestre(
        codigo,
        fechaInicioClases,
        fechaFinClases,
        fechaInicioInscripcion,
        fechaFinInscripcion,
        fechaInicioIntegradores,
        fechaFinIntegradores);

    assertNotNull(cuatrimestre);
    assertEquals(codigo, cuatrimestre.getCodigo());
    assertEquals(fechaInicioClases, cuatrimestre.getFechaDeInicioClases());
    assertEquals(fechaFinClases, cuatrimestre.getFechaDeFinClases());
    assertEquals(fechaInicioInscripcion, cuatrimestre.getFechaInicioPeriodoDeInscripcion());
    assertEquals(fechaFinInscripcion, cuatrimestre.getFechaFinPeriodoDeInscripcion());
    assertEquals(fechaInicioIntegradores, cuatrimestre.getFechaInicioPeriodoIntegradores());
    assertEquals(fechaFinIntegradores, cuatrimestre.getFechaFinPeriodoIntegradores());
    assertNotNull(cuatrimestre.getCursos());
    assertTrue(cuatrimestre.getCursos().isEmpty());
  }

  @Test
  void crearYVerificarRelacionConCursos() {
    String codigo = "2024-2";
    LocalDate fechaInicioClases = LocalDate.of(2024, 8, 1);
    LocalDate fechaFinClases = LocalDate.of(2024, 12, 15);
    LocalDate fechaInicioInscripcion = LocalDate.of(2024, 7, 1);
    LocalDate fechaFinInscripcion = LocalDate.of(2024, 7, 31);
    LocalDate fechaInicioIntegradores = LocalDate.of(2024, 12, 16);
    LocalDate fechaFinIntegradores = LocalDate.of(2024, 12, 31);

    Cuatrimestre cuatrimestre = new Cuatrimestre(
        codigo,
        fechaInicioClases,
        fechaFinClases,
        fechaInicioInscripcion,
        fechaFinInscripcion,
        fechaInicioIntegradores,
        fechaFinIntegradores);

    Materia materia = new Materia();
    materia.setCodigoDeMateria("123-M");
    materia.setNombre("Análisis I");

    List<Cuatrimestre> cuatrimestres = new ArrayList<>();
    cuatrimestres.add(cuatrimestre);

    Curso curso = new Curso("CURSO-001", 30, materia, cuatrimestres);
    
    List<Curso> cursos = new ArrayList<>();
    cursos.add(curso);
    cuatrimestre.setCursos(cursos);

    assertNotNull(cuatrimestre.getCursos());
    assertEquals(1, cuatrimestre.getCursos().size());
    assertEquals("CURSO-001", cuatrimestre.getCursos().get(0).getCodigo());
  }

  @Test
  void verificarSettersYGetters() {
    Cuatrimestre cuatrimestre = new Cuatrimestre();
    
    String codigo = "2025-1";
    LocalDate fechaInicioClases = LocalDate.of(2025, 3, 1);
    LocalDate fechaFinClases = LocalDate.of(2025, 7, 15);
    LocalDate fechaInicioInscripcion = LocalDate.of(2025, 2, 1);
    LocalDate fechaFinInscripcion = LocalDate.of(2025, 2, 28);
    LocalDate fechaInicioIntegradores = LocalDate.of(2025, 7, 16);
    LocalDate fechaFinIntegradores = LocalDate.of(2025, 7, 31);

    cuatrimestre.setCodigo(codigo);
    cuatrimestre.setFechaDeInicioClases(fechaInicioClases);
    cuatrimestre.setFechaDeFinClases(fechaFinClases);
    cuatrimestre.setFechaInicioPeriodoDeInscripcion(fechaInicioInscripcion);
    cuatrimestre.setFechaFinPeriodoDeInscripcion(fechaFinInscripcion);
    cuatrimestre.setFechaInicioPeriodoIntegradores(fechaInicioIntegradores);
    cuatrimestre.setFechaFinPeriodoIntegradores(fechaFinIntegradores);

    assertEquals(codigo, cuatrimestre.getCodigo());
    assertEquals(fechaInicioClases, cuatrimestre.getFechaDeInicioClases());
    assertEquals(fechaFinClases, cuatrimestre.getFechaDeFinClases());
    assertEquals(fechaInicioInscripcion, cuatrimestre.getFechaInicioPeriodoDeInscripcion());
    assertEquals(fechaFinInscripcion, cuatrimestre.getFechaFinPeriodoDeInscripcion());
    assertEquals(fechaInicioIntegradores, cuatrimestre.getFechaInicioPeriodoIntegradores());
    assertEquals(fechaFinIntegradores, cuatrimestre.getFechaFinPeriodoIntegradores());
  }
}