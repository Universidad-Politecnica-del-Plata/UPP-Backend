package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.upp.dto.AlumnoDTO;
import com.upp.exception.AlumnoExisteException;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.model.Alumno;
import com.upp.model.Carrera;
import com.upp.model.PlanDeEstudios;
import com.upp.model.Rol;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.CarreraRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import com.upp.repository.RolRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

class AlumnoServiceTest {

  @Mock private AlumnoRepository alumnoRepository;
  @Mock private RolRepository rolRepository;
  @Mock private CarreraRepository carreraRepository;
  @Mock private PlanDeEstudiosRepository planDeEstudiosRepository;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private AlumnoService alumnoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void crearAlumnoCuandoNoExisteAlumnoGuardaCorrectamente() {
    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setNombre("Juan");
    alumnoDTO.setApellido("Perez");
    alumnoDTO.setDni(12345678L);
    alumnoDTO.setEmail("juan.perez@example.com");
    alumnoDTO.setDireccion("Calle Falsa 123");
    alumnoDTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
    alumnoDTO.setFechaIngreso(LocalDate.of(2023, 3, 1));
    alumnoDTO.setTelefonos(Arrays.asList("123456789"));
    alumnoDTO.setCodigosCarreras(Arrays.asList("CS"));
    alumnoDTO.setCodigosPlanesDeEstudio(Arrays.asList("2023"));

    when(alumnoRepository.existsByDniOrEmail(alumnoDTO.getDni(), alumnoDTO.getEmail()))
        .thenReturn(false);
    when(rolRepository.findById("ROLE_ALUMNO")).thenReturn(Optional.of(new Rol("ROLE_ALUMNO")));
    when(carreraRepository.findByCodigoDeCarrera("CS")).thenReturn(Optional.of(new Carrera()));
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios("2023")).thenReturn(Optional.of(new PlanDeEstudios()));
    when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("passwordEncriptado");

    Alumno alumnoGuardado = new Alumno();
    alumnoGuardado.setNombre(alumnoDTO.getNombre());
    alumnoGuardado.setApellido(alumnoDTO.getApellido());
    alumnoGuardado.setDni(alumnoDTO.getDni());
    alumnoGuardado.setEmail(alumnoDTO.getEmail());
    alumnoGuardado.setDireccion(alumnoDTO.getDireccion());
    alumnoGuardado.setFechaNacimiento(alumnoDTO.getFechaNacimiento());
    alumnoGuardado.setFechaIngreso(alumnoDTO.getFechaIngreso());
    alumnoGuardado.setTelefonos(alumnoDTO.getTelefonos());
    alumnoGuardado.setCarreras(Arrays.asList(new Carrera()));
    alumnoGuardado.setPlanesDeEstudio(Arrays.asList(new PlanDeEstudios()));
    alumnoGuardado.setMatricula(1L);

    when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoGuardado);

    AlumnoDTO resultado = alumnoService.crearAlumno(alumnoDTO);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getMatricula());
    assertEquals("Juan", resultado.getNombre());

    verify(alumnoRepository, times(1)).existsByDniOrEmail(alumnoDTO.getDni(), alumnoDTO.getEmail());
    verify(alumnoRepository, times(1)).save(any(Alumno.class));
  }

  @Test
  void crearAlumnoConAlumnoExistenteLanzaExcepcion() {
    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setDni(12345678L);
    alumnoDTO.setEmail("juan.perez@example.com");

    when(alumnoRepository.existsByDniOrEmail(alumnoDTO.getDni(), alumnoDTO.getEmail()))
        .thenReturn(true);

    assertThrows(
        AlumnoExisteException.class,
        () -> {
          alumnoService.crearAlumno(alumnoDTO);
        });

    verify(alumnoRepository, times(1)).existsByDniOrEmail(alumnoDTO.getDni(), alumnoDTO.getEmail());
    verify(alumnoRepository, never()).save(any(Alumno.class));
  }

  @Test
  void obtenerAlumnoPorMatriculaDevuelveAlumnoSiExiste() {
    Long matricula = 12345L;
    Alumno alumno = new Alumno();
    alumno.setMatricula(matricula);
    alumno.setNombre("Juan");
    alumno.setApellido("Perez");
    alumno.setDni(87654321L);
    alumno.setEmail("juan@example.com");
    alumno.setCarreras(Arrays.asList(new Carrera()));
    alumno.setPlanesDeEstudio(Arrays.asList(new PlanDeEstudios()));

    when(alumnoRepository.findByMatricula(matricula)).thenReturn(Optional.of(alumno));

    AlumnoDTO resultado = alumnoService.obtenerAlumnoPorMatricula(matricula);

    assertNotNull(resultado);
    assertEquals(matricula, resultado.getMatricula());
    assertEquals("Juan", resultado.getNombre());
    assertEquals("Perez", resultado.getApellido());
    verify(alumnoRepository, times(1)).findByMatricula(matricula);
  }

  @Test
  void obtenerAlumnoPorMatriculaLanzaExcepcionSiNoExiste() {
    Long matricula = 99999L;
    when(alumnoRepository.findByMatricula(matricula)).thenReturn(Optional.empty());

    assertThrows(
        AlumnoNoExisteException.class,
        () -> alumnoService.obtenerAlumnoPorMatricula(matricula));

    verify(alumnoRepository, times(1)).findByMatricula(matricula);
  }

  @Test
  void modificarAlumnoActualizaCorrectamenteSiExiste() {
    Long matricula = 12345L;
    Alumno alumnoExistente = new Alumno();
    alumnoExistente.setMatricula(matricula);
    alumnoExistente.setNombre("Juan");
    alumnoExistente.setApellido("Perez");

    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setMatricula(matricula);
    alumnoDTO.setNombre("Juan Carlos");
    alumnoDTO.setApellido("Perez Gomez");
    alumnoDTO.setDni(87654321L);
    alumnoDTO.setEmail("juan.carlos@example.com");
    alumnoDTO.setCodigosCarreras(Arrays.asList("CS"));
    alumnoDTO.setCodigosPlanesDeEstudio(Arrays.asList("2023"));

    when(alumnoRepository.findByMatricula(matricula)).thenReturn(Optional.of(alumnoExistente));
    when(carreraRepository.findByCodigoDeCarrera("CS")).thenReturn(Optional.of(new Carrera()));
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios("2023")).thenReturn(Optional.of(new PlanDeEstudios()));

    AlumnoDTO resultado = alumnoService.modificarAlumno(matricula, alumnoDTO);

    assertEquals(alumnoDTO, resultado);
    verify(alumnoRepository, times(1)).findByMatricula(matricula);
    verify(alumnoRepository, times(1)).save(alumnoExistente);
  }

  @Test
  void modificarAlumnoLanzaExcepcionSiNoExiste() {
    Long matricula = 99999L;
    AlumnoDTO alumnoDTO = new AlumnoDTO();
    when(alumnoRepository.findByMatricula(matricula)).thenReturn(Optional.empty());

    assertThrows(
        AlumnoNoExisteException.class,
        () -> alumnoService.modificarAlumno(matricula, alumnoDTO));

    verify(alumnoRepository, times(1)).findByMatricula(matricula);
    verify(alumnoRepository, never()).save(any(Alumno.class));
  }

  @Test
  void eliminarAlumnoRealizaBajaLogicaSiExiste() {
    Long matricula = 12345L;
    Alumno alumno = new Alumno();
    alumno.setMatricula(matricula);
    alumno.setHabilitado(true);

    when(alumnoRepository.findByMatricula(matricula)).thenReturn(Optional.of(alumno));

    alumnoService.eliminarAlumno(matricula);

    assertFalse(alumno.isHabilitado());
    verify(alumnoRepository, times(1)).findByMatricula(matricula);
    verify(alumnoRepository, times(1)).save(alumno);
  }

  @Test
  void eliminarAlumnoLanzaExcepcionSiNoExiste() {
    Long matricula = 99999L;
    when(alumnoRepository.findByMatricula(matricula)).thenReturn(Optional.empty());

    assertThrows(
        AlumnoNoExisteException.class,
        () -> alumnoService.eliminarAlumno(matricula));

    verify(alumnoRepository, times(1)).findByMatricula(matricula);
    verify(alumnoRepository, never()).save(any(Alumno.class));
  }
}
