package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.upp.dto.AlumnoDTO;
import com.upp.exception.AlumnoExisteException;
import com.upp.model.Alumno;
import com.upp.repository.AlumnoRepository;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class AlumnoServiceTest {

  @Mock private AlumnoRepository alumnoRepository;

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

    Alumno alumnoGuardado = new Alumno();
    alumnoGuardado.setMatricula(1L);
    alumnoGuardado.setNombre(alumnoDTO.getNombre());
    alumnoGuardado.setApellido(alumnoDTO.getApellido());
    alumnoGuardado.setDni(alumnoDTO.getDni());
    alumnoGuardado.setEmail(alumnoDTO.getEmail());
    alumnoGuardado.setDireccion(alumnoDTO.getDireccion());
    alumnoGuardado.setFechaNacimiento(alumnoDTO.getFechaNacimiento());
    alumnoGuardado.setFechaIngreso(alumnoDTO.getFechaIngreso());
    alumnoGuardado.setTelefonos(alumnoDTO.getTelefonos());
    alumnoGuardado.setCarreras(alumnoDTO.getCodigosCarreras());
    alumnoGuardado.setPlanesDeEstudio(alumnoDTO.getCodigosPlanesDeEstudio());

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
}
