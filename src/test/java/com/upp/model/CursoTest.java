package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CursoTest {
  @Test
  void crearSinParametros() {

    new Curso();
  }

  @Test
  void crearConParametros() {

    String codigo = "CURSO-001";
    Integer maximoDeAlumnos = 30;
    Materia materia = new Materia();
    materia.setCodigoDeMateria("123-M");
    materia.setNombre("Análisis I");

    Curso curso = new Curso(codigo, maximoDeAlumnos, materia);
    assertNotNull(curso);
    assertEquals(codigo, curso.getCodigo());
    assertEquals(maximoDeAlumnos, curso.getMaximoDeAlumnos());
    assertEquals(materia, curso.getMateria());
    assertNotNull(curso.getCuatrimestres());
    assertTrue(curso.getCuatrimestres().isEmpty());
  }

  @Test
  void crearConParametrosYCuatrimestres() {
    String codigo = "CURSO-002";
    Integer maximoDeAlumnos = 25;
    Materia materia = new Materia();
    materia.setCodigoDeMateria("124-M");
    materia.setNombre("Análisis II");

    List<Cuatrimestre> cuatrimestres = new ArrayList<>();
    Cuatrimestre cuatrimestre1 = new Cuatrimestre();
    cuatrimestre1.setCodigo("2024-1");
    cuatrimestres.add(cuatrimestre1);

    Curso curso = new Curso(codigo, maximoDeAlumnos, materia, cuatrimestres);
    assertNotNull(curso);
    assertEquals(codigo, curso.getCodigo());
    assertEquals(maximoDeAlumnos, curso.getMaximoDeAlumnos());
    assertEquals(materia, curso.getMateria());
    assertEquals(1, curso.getCuatrimestres().size());
    assertEquals("2024-1", curso.getCuatrimestres().get(0).getCodigo());
  }

  @Test
  void crearConCuatrimestreNull() {
    String codigo = "CURSO-003";
    Integer maximoDeAlumnos = 20;
    Materia materia = new Materia();
    materia.setCodigoDeMateria("125-M");
    materia.setNombre("Álgebra I");

    Curso curso = new Curso(codigo, maximoDeAlumnos, materia, null);
    assertNotNull(curso);
    assertNotNull(curso.getCuatrimestres());
    assertTrue(curso.getCuatrimestres().isEmpty());
  }
}
