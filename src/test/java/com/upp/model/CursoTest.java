package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
  }

 
}