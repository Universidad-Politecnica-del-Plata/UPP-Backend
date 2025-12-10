package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.upp.dto.EstadoAcademicoDTO;
import com.upp.dto.HistoriaAcademicaDTO;
import com.upp.dto.MateriaAprobadaDTO;
import com.upp.model.Acta;
import com.upp.model.Alumno;
import com.upp.model.Curso;
import com.upp.model.Materia;
import com.upp.model.Nota;
import com.upp.model.TipoDeActa;
import com.upp.model.TipoMateria;
import com.upp.repository.NotaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HistoriaAcademicaServiceTest {

  @Mock
  private NotaRepository notaRepository;

  @InjectMocks
  private HistoriaAcademicaService historiaAcademicaService;

  private Alumno alumno;
  private Materia matematica;
  private Materia fisica;
  private Materia calculo;
  private Nota notaMatematica;
  private Nota notaFisica;
  private Acta actaFinalMatematica;
  private Acta actaFinalFisica;

  @BeforeEach
  void setUp() {
    alumno = new Alumno();
    alumno.setId(1L);
    alumno.setNombre("Juan");
    alumno.setApellido("Pérez");
    alumno.setMatricula(100001L);

    // Materia Matemática (sin correlativas)
    matematica = new Materia();
    matematica.setCodigoDeMateria("MAT001");
    matematica.setNombre("Matemática I");
    matematica.setCreditosQueOtorga(8);
    matematica.setCreditosNecesarios(0);
    matematica.setTipo(TipoMateria.OBLIGATORIA);
    matematica.setCorrelativas(new ArrayList<>());

    // Materia Física (sin correlativas)
    fisica = new Materia();
    fisica.setCodigoDeMateria("FIS001");
    fisica.setNombre("Física I");
    fisica.setCreditosQueOtorga(6);
    fisica.setCreditosNecesarios(0);
    fisica.setTipo(TipoMateria.OBLIGATORIA);
    fisica.setCorrelativas(new ArrayList<>());

    // Materia Cálculo (requiere Matemática)
    calculo = new Materia();
    calculo.setCodigoDeMateria("CAL001");
    calculo.setNombre("Cálculo I");
    calculo.setCreditosQueOtorga(10);
    calculo.setCreditosNecesarios(8);
    calculo.setTipo(TipoMateria.OBLIGATORIA);
    calculo.setCorrelativas(Arrays.asList(matematica));

    // Crear actas FINAL
    actaFinalMatematica = new Acta();
    actaFinalMatematica.setNumeroCorrelativo(1L);
    actaFinalMatematica.setTipoDeActa(TipoDeActa.FINAL);
    actaFinalMatematica.setFechaDeCreacion(LocalDateTime.now().minusMonths(2));

    actaFinalFisica = new Acta();
    actaFinalFisica.setNumeroCorrelativo(2L);
    actaFinalFisica.setTipoDeActa(TipoDeActa.FINAL);
    actaFinalFisica.setFechaDeCreacion(LocalDateTime.now().minusMonths(1));

    // Crear cursos
    Curso cursoMatematica = new Curso();
    cursoMatematica.setMateria(matematica);
    actaFinalMatematica.setCurso(cursoMatematica);

    Curso cursoFisica = new Curso();
    cursoFisica.setMateria(fisica);
    actaFinalFisica.setCurso(cursoFisica);

    // Crear notas aprobadas
    notaMatematica = new Nota();
    notaMatematica.setId(1L);
    notaMatematica.setValor(8);
    notaMatematica.setAlumno(alumno);
    notaMatematica.setActa(actaFinalMatematica);

    notaFisica = new Nota();
    notaFisica.setId(2L);
    notaFisica.setValor(7);
    notaFisica.setAlumno(alumno);
    notaFisica.setActa(actaFinalFisica);
  }

  @Test
  void calcularCreditosAcumuladosConNotasAprobadas() {
    List<Nota> notasAprobadas = Arrays.asList(notaMatematica, notaFisica);
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(notasAprobadas);

    Integer creditos = historiaAcademicaService.calcularCreditosAcumulados(alumno);

    assertEquals(14, creditos); // 8 + 6
    verify(notaRepository).findNotasAprobadasEnActasFinalesByAlumno(alumno);
  }

  @Test
  void calcularCreditosAcumuladosSinNotas() {
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(new ArrayList<>());

    Integer creditos = historiaAcademicaService.calcularCreditosAcumulados(alumno);

    assertEquals(0, creditos);
    verify(notaRepository).findNotasAprobadasEnActasFinalesByAlumno(alumno);
  }

  @Test
  void verificarMateriaAprobadaExitoso() {
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(true);

    boolean resultado = historiaAcademicaService.verificarMateriaAprobada(alumno, "MAT001");

    assertTrue(resultado);
    verify(notaRepository).existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL);
  }

  @Test
  void verificarMateriaAprobadaNoAprobada() {
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(false);

    boolean resultado = historiaAcademicaService.verificarMateriaAprobada(alumno, "MAT001");

    assertFalse(resultado);
  }

  @Test
  void verificarCorrelativasAprobadasSinCorrelativas() {
    boolean resultado = historiaAcademicaService.verificarCorrelativasAprobadas(alumno, matematica);

    assertTrue(resultado);
    verify(notaRepository, never()).existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(any(), any(), any());
  }

  @Test
  void verificarCorrelativasAprobadasTodasAprobadas() {
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(true);

    boolean resultado = historiaAcademicaService.verificarCorrelativasAprobadas(alumno, calculo);

    assertTrue(resultado);
    verify(notaRepository).existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL);
  }

  @Test
  void verificarCorrelativasAprobadasConPendientes() {
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(false);

    boolean resultado = historiaAcademicaService.verificarCorrelativasAprobadas(alumno, calculo);

    assertFalse(resultado);
  }

  @Test
  void obtenerCorrelativasNoAprobadasConPendientes() {
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(false);

    List<String> correlativasNoAprobadas = historiaAcademicaService.obtenerCorrelativasNoAprobadas(alumno, calculo);

    assertEquals(1, correlativasNoAprobadas.size());
    assertEquals("MAT001 - Matemática I", correlativasNoAprobadas.get(0));
  }

  @Test
  void obtenerCorrelativasNoAprobadasTodasAprobadas() {
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(true);

    List<String> correlativasNoAprobadas = historiaAcademicaService.obtenerCorrelativasNoAprobadas(alumno, calculo);

    assertTrue(correlativasNoAprobadas.isEmpty());
  }

  @Test
  void obtenerMateriasAprobadas() {
    List<Nota> notasAprobadas = Arrays.asList(notaMatematica, notaFisica);
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(notasAprobadas);

    List<MateriaAprobadaDTO> materiasAprobadas = historiaAcademicaService.obtenerMateriasAprobadas(alumno);

    assertEquals(2, materiasAprobadas.size());
    
    MateriaAprobadaDTO materiaDTO1 = materiasAprobadas.get(0);
    assertEquals("MAT001", materiaDTO1.getCodigoMateria());
    assertEquals("Matemática I", materiaDTO1.getNombre());
    assertEquals(8, materiaDTO1.getNota());
    assertEquals(8, materiaDTO1.getCreditosQueOtorga());
    assertEquals(1L, materiaDTO1.getNumeroCorrelativoActa());

    MateriaAprobadaDTO materiaDTO2 = materiasAprobadas.get(1);
    assertEquals("FIS001", materiaDTO2.getCodigoMateria());
    assertEquals("Física I", materiaDTO2.getNombre());
    assertEquals(7, materiaDTO2.getNota());
    assertEquals(6, materiaDTO2.getCreditosQueOtorga());
    assertEquals(2L, materiaDTO2.getNumeroCorrelativoActa());
  }

  @Test
  void obtenerHistoriaAcademica() {
    List<Nota> notasAprobadas = Arrays.asList(notaMatematica, notaFisica);
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(notasAprobadas);

    HistoriaAcademicaDTO historia = historiaAcademicaService.obtenerHistoriaAcademica(alumno);

    assertEquals(1L, historia.getAlumnoId());
    assertEquals("Juan Pérez", historia.getNombreCompleto());
    assertEquals(100001L, historia.getMatricula());
    assertEquals(14, historia.getCreditosAcumulados());
    assertEquals(2, historia.getMateriasAprobadas().size());
  }

  @Test
  void evaluarEstadoParaInscripcionPuedeInscribirse() {
    List<Nota> notasAprobadas = Arrays.asList(notaMatematica);
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(notasAprobadas);
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(true);

    EstadoAcademicoDTO estado = historiaAcademicaService.evaluarEstadoParaInscripcion(alumno, calculo);

    assertTrue(estado.isPuedeInscribirse());
    assertNull(estado.getMotivoNoInscripcion());
    assertEquals(8, estado.getCreditosAcumulados());
    assertEquals(8, estado.getCreditosTotalesRequeridos());
    assertTrue(estado.getCorrelativasNoAprobadas().isEmpty());
  }

  @Test
  void evaluarEstadoParaInscripcionCreditosInsuficientes() {
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(new ArrayList<>());

    EstadoAcademicoDTO estado = historiaAcademicaService.evaluarEstadoParaInscripcion(alumno, calculo);

    assertFalse(estado.isPuedeInscribirse());
    assertNotNull(estado.getMotivoNoInscripcion());
    assertTrue(estado.getMotivoNoInscripcion().contains("Créditos insuficientes"));
    assertEquals(0, estado.getCreditosAcumulados());
    assertEquals(8, estado.getCreditosTotalesRequeridos());
  }

  @Test
  void evaluarEstadoParaInscripcionCorrelativasNoAprobadas() {
    List<Nota> notasAprobadas = Arrays.asList(notaFisica);
    when(notaRepository.findNotasAprobadasEnActasFinalesByAlumno(alumno))
        .thenReturn(notasAprobadas);
    when(notaRepository.existsNotaAprobadaByAlumnoAndMateriaAndActaTipo(
        alumno, "MAT001", TipoDeActa.FINAL))
        .thenReturn(false);

    EstadoAcademicoDTO estado = historiaAcademicaService.evaluarEstadoParaInscripcion(alumno, calculo);

    assertFalse(estado.isPuedeInscribirse());
    assertNotNull(estado.getMotivoNoInscripcion());
    assertTrue(estado.getMotivoNoInscripcion().contains("Correlativas no aprobadas"));
    assertEquals(6, estado.getCreditosAcumulados());
    assertEquals(8, estado.getCreditosTotalesRequeridos());
    assertEquals(1, estado.getCorrelativasNoAprobadas().size());
  }
}