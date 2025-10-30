package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class InscripcionTest {

  @Test
  void crearSinParametros() {
    new Inscripcion();
  }

  @Test
  void crearConParametros() {
    Curso curso = new Curso("CURSO-001", 30, createMateria());
    Cuatrimestre cuatrimestre = createCuatrimestre();
    Alumno alumno = createAlumno();

    LocalDate fechaAntes = LocalDate.now();
    LocalTime horaAntes = LocalTime.now();

    Inscripcion inscripcion = new Inscripcion(curso, cuatrimestre, alumno);

    assertNotNull(inscripcion);
    assertEquals(curso, inscripcion.getCurso());
    assertEquals(cuatrimestre, inscripcion.getCuatrimestre());
    assertEquals(alumno, inscripcion.getAlumno());
    assertNotNull(inscripcion.getFecha());
    assertNotNull(inscripcion.getHorario());
    
    LocalDate fechaDespues = LocalDate.now();
    LocalTime horaDespues = LocalTime.now();
    
    assertEquals(fechaAntes, inscripcion.getFecha());
    assertEquals(horaAntes.getHour(), inscripcion.getHorario().getHour());
    assertEquals(horaAntes.getMinute(), inscripcion.getHorario().getMinute());
  }

  @Test
  void testGettersYSetters() {
    Inscripcion inscripcion = new Inscripcion();
    
    Long codigoDeInscripcion = 123L;
    LocalDate fecha = LocalDate.of(2024, 10, 24);
    LocalTime horario = LocalTime.of(14, 30);
    Curso curso = new Curso("CURSO-002", 25, createMateria());
    Cuatrimestre cuatrimestre = createCuatrimestre();
    Alumno alumno = createAlumno();

    inscripcion.setCodigoDeInscripcion(codigoDeInscripcion);
    inscripcion.setFecha(fecha);
    inscripcion.setHorario(horario);
    inscripcion.setCurso(curso);
    inscripcion.setCuatrimestre(cuatrimestre);
    inscripcion.setAlumno(alumno);

    assertEquals(codigoDeInscripcion, inscripcion.getCodigoDeInscripcion());
    assertEquals(fecha, inscripcion.getFecha());
    assertEquals(horario, inscripcion.getHorario());
    assertEquals(curso, inscripcion.getCurso());
    assertEquals(cuatrimestre, inscripcion.getCuatrimestre());
    assertEquals(alumno, inscripcion.getAlumno());
  }

  private Materia createMateria() {
    Materia materia = new Materia();
    materia.setCodigoDeMateria("123-M");
    materia.setNombre("Análisis I");
    return materia;
  }

  private Cuatrimestre createCuatrimestre() {
    Cuatrimestre cuatrimestre = new Cuatrimestre();
    cuatrimestre.setCodigo("2024-1");
    cuatrimestre.setFechaDeInicioClases(LocalDate.of(2024, 3, 1));
    cuatrimestre.setFechaDeFinClases(LocalDate.of(2024, 7, 15));
    cuatrimestre.setFechaInicioPeriodoDeInscripcion(LocalDate.of(2024, 2, 1));
    cuatrimestre.setFechaFinPeriodoDeInscripcion(LocalDate.of(2024, 2, 28));
    cuatrimestre.setFechaInicioPeriodoIntegradores(LocalDate.of(2024, 7, 16));
    cuatrimestre.setFechaFinPeriodoIntegradores(LocalDate.of(2024, 8, 15));
    return cuatrimestre;
  }

  private Alumno createAlumno() {
    Alumno alumno = new Alumno();
    alumno.setId(1L);
    alumno.setMatricula(100001L);
    alumno.setNombre("Juan");
    alumno.setApellido("Pérez");
    alumno.setDni(12345678L);
    alumno.setEmail("juan.perez@example.com");
    alumno.setUsername("jperez");
    alumno.setPassword("password123");
    alumno.setTelefonos(new ArrayList<>());
    alumno.setCarreras(new ArrayList<>());
    alumno.setPlanesDeEstudio(new ArrayList<>());
    return alumno;
  }
}