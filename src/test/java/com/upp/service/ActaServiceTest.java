package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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
import com.upp.exception.InscripcionExisteException;
import com.upp.exception.NotaInvalidaException;
import com.upp.exception.NotaNoExisteException;
import com.upp.model.Acta;
import com.upp.model.Alumno;
import com.upp.model.Cuatrimestre;
import com.upp.model.Curso;
import com.upp.model.EstadoActa;
import com.upp.model.Inscripcion;
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
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
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
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
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
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
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
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
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
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
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
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
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

  @Test
  void obtenerTodasLasActasExitoso() {
    List<Acta> actas = List.of(acta);
    when(actaRepository.findAll()).thenReturn(actas);

    List<ActaDTO> resultado = actaService.obtenerTodasLasActas();

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals(1L, resultado.get(0).getNumeroCorrelativo());
    assertEquals(TipoDeActa.CURSADA, resultado.get(0).getTipoDeActa());
    verify(actaRepository).findAll();
  }

  @Test
  void obtenerTodasLasActasListaVacia() {
    when(actaRepository.findAll()).thenReturn(new ArrayList<>());

    List<ActaDTO> resultado = actaService.obtenerTodasLasActas();

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verify(actaRepository).findAll();
  }

  @Test
  void agregarNotasMasivasExitoso() {
    // Datos de test
    Alumno alumno2 = new Alumno();
    alumno2.setId(2L);
    alumno2.setMatricula(100002L);
    alumno2.setNombre("María");
    alumno2.setApellido("González");

    NotaRequestDTO notaRequest1 = new NotaRequestDTO(8, 1L);
    NotaRequestDTO notaRequest2 = new NotaRequestDTO(9, 2L);
    List<NotaRequestDTO> notasRequest = List.of(notaRequest1, notaRequest2);
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(notasRequest);

    // Mocks
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(alumnoRepository.findById(2L)).thenReturn(Optional.of(alumno2));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(
        alumno, curso, cuatrimestre)).thenReturn(true);
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(
        alumno2, curso, cuatrimestre)).thenReturn(true);
    when(notaRepository.existsByActaAndAlumno(acta, alumno)).thenReturn(false);
    when(notaRepository.existsByActaAndAlumno(acta, alumno2)).thenReturn(false);
    when(notaRepository.save(any(Nota.class)))
        .thenAnswer(invocation -> {
          Nota savedNota = invocation.getArgument(0);
          savedNota.setId(savedNota.getAlumno().getId()); // ID de la nota = ID del alumno para test
          return savedNota;
        });

    // Ejecución
    List<NotaDTO> resultado = actaService.agregarNotasMasivas(1L, notasMasivasRequest);

    // Verificaciones
    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    
    assertEquals(1L, resultado.get(0).getId());
    assertEquals(8, resultado.get(0).getValor());
    assertEquals(1L, resultado.get(0).getAlumnoId());
    assertEquals("Juan", resultado.get(0).getNombreAlumno());
    
    assertEquals(2L, resultado.get(1).getId());
    assertEquals(9, resultado.get(1).getValor());
    assertEquals(2L, resultado.get(1).getAlumnoId());
    assertEquals("María", resultado.get(1).getNombreAlumno());

    verify(notaRepository, times(2)).save(any(Nota.class));
  }

  @Test
  void agregarNotasMasivasActaNoExisteLanzaExcepcion() {
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(
        List.of(new NotaRequestDTO(8, 1L)));
    
    when(actaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ActaNoExisteException.class, 
        () -> actaService.agregarNotasMasivas(1L, notasMasivasRequest));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotasMasivasActaCerradaLanzaExcepcion() {
    acta.setEstado(EstadoActa.CERRADA);
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(
        List.of(new NotaRequestDTO(8, 1L)));
    
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));

    assertThrows(ActaCerradaException.class, 
        () -> actaService.agregarNotasMasivas(1L, notasMasivasRequest));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotasMasivasNotaInvalidaLanzaExcepcion() {
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(
        List.of(new NotaRequestDTO(3, 1L))); // Nota inválida

    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
          .thenReturn(List.of(cuatrimestre));
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));

    assertThrows(NotaInvalidaException.class, 
        () -> actaService.agregarNotasMasivas(1L, notasMasivasRequest));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotasMasivasAlumnoNoExisteLanzaExcepcion() {
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(
        List.of(new NotaRequestDTO(8, 999L))); // Alumno no existe
    
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(AlumnoNoExisteException.class, 
        () -> actaService.agregarNotasMasivas(1L, notasMasivasRequest));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotasMasivasAlumnoNoInscriptoLanzaExcepcion() {
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(
        List.of(new NotaRequestDTO(8, 1L)));
    
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(
        alumno, curso, cuatrimestre)).thenReturn(false);

    assertThrows(AlumnoNoInscriptoException.class, 
        () -> actaService.agregarNotasMasivas(1L, notasMasivasRequest));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void agregarNotasMasivasNotaYaExisteLanzaExcepcion() {
    NotasMasivasRequestDTO notasMasivasRequest = new NotasMasivasRequestDTO(
        List.of(new NotaRequestDTO(8, 1L)));
    
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(any(LocalDate.class)))
        .thenReturn(List.of(cuatrimestre));
    when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
    when(inscripcionRepository.existsByAlumnoAndCursoAndCuatrimestre(
        alumno, curso, cuatrimestre)).thenReturn(true);
    when(notaRepository.existsByActaAndAlumno(acta, alumno)).thenReturn(true);

    assertThrows(InscripcionExisteException.class, 
        () -> actaService.agregarNotasMasivas(1L, notasMasivasRequest));

    verify(notaRepository, never()).save(any(Nota.class));
  }

  @Test
  void obtenerAlumnosInscriptosPorActaExitoso() {
    // Crear datos de test adicionales
    Alumno alumno2 = new Alumno();
    alumno2.setId(2L);
    alumno2.setMatricula(100002L);
    alumno2.setNombre("María");
    alumno2.setApellido("González");
    alumno2.setEmail("maria.gonzalez@example.com");

    Inscripcion inscripcion1 = new Inscripcion();
    inscripcion1.setAlumno(alumno);
    inscripcion1.setCurso(curso);
    inscripcion1.setCuatrimestre(cuatrimestre);

    Inscripcion inscripcion2 = new Inscripcion();
    inscripcion2.setAlumno(alumno2);
    inscripcion2.setCurso(curso);
    inscripcion2.setCuatrimestre(cuatrimestre);

    List<Inscripcion> inscripciones = List.of(inscripcion1, inscripcion2);

    // Mocks
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(acta.getFechaDeCreacion().toLocalDate()))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.findByCursoAndCuatrimestre(curso, cuatrimestre))
        .thenReturn(inscripciones);

    // Ejecución
    List<AlumnoInscriptoDTO> resultado = actaService.obtenerAlumnosInscriptosPorActa(1L);

    // Verificaciones
    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    
    AlumnoInscriptoDTO alumnoDTO1 = resultado.get(0);
    assertEquals(1L, alumnoDTO1.getId());
    assertEquals("Juan", alumnoDTO1.getNombre());
    assertEquals("Pérez", alumnoDTO1.getApellido());
    assertEquals(100001L, alumnoDTO1.getMatricula());
    assertEquals("juan.perez@example.com", alumnoDTO1.getEmail());
    
    AlumnoInscriptoDTO alumnoDTO2 = resultado.get(1);
    assertEquals(2L, alumnoDTO2.getId());
    assertEquals("María", alumnoDTO2.getNombre());
    assertEquals("González", alumnoDTO2.getApellido());
    assertEquals(100002L, alumnoDTO2.getMatricula());
    assertEquals("maria.gonzalez@example.com", alumnoDTO2.getEmail());
  }

  @Test
  void obtenerAlumnosInscriptosPorActaNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ActaNoExisteException.class, 
        () -> actaService.obtenerAlumnosInscriptosPorActa(1L));
  }

  @Test
  void obtenerAlumnosInscriptosPorActaCuatrimestreNoExisteLanzaExcepcion() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(acta.getFechaDeCreacion().toLocalDate()))
        .thenReturn(new ArrayList<>());

    assertThrows(CuatrimestreNoExisteException.class, 
        () -> actaService.obtenerAlumnosInscriptosPorActa(1L));
  }

  @Test
  void obtenerAlumnosInscriptosPorActaListaVacia() {
    when(actaRepository.findById(1L)).thenReturn(Optional.of(acta));
    when(cuatrimestreRepository.findCuatrimestresByFecha(acta.getFechaDeCreacion().toLocalDate()))
        .thenReturn(List.of(cuatrimestre));
    when(inscripcionRepository.findByCursoAndCuatrimestre(curso, cuatrimestre))
        .thenReturn(new ArrayList<>());

    List<AlumnoInscriptoDTO> resultado = actaService.obtenerAlumnosInscriptosPorActa(1L);

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }
}
