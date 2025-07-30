package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.upp.dto.CarreraDTO;
import com.upp.exception.CarreraExisteException;
import com.upp.exception.CarreraNoExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.model.Carrera;
import com.upp.model.PlanDeEstudios;
import com.upp.repository.CarreraRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CarreraServiceTest {
  private CarreraRepository carreraRepository;
  private PlanDeEstudiosRepository planDeEstudiosRepository;
  private CarreraService carreraService;

  @BeforeEach
  void setUp() {
    carreraRepository = mock(CarreraRepository.class);
    planDeEstudiosRepository = mock(PlanDeEstudiosRepository.class);
    carreraService = new CarreraService(carreraRepository, planDeEstudiosRepository);
  }

  @Test
  void crearCarreraConCodigoExistenteLanzaExcepcion() {
    CarreraDTO dto = new CarreraDTO();
    dto.setCodigoDeCarrera("ING-SIS");

    when(carreraRepository.existsByCodigoDeCarrera("ING-SIS")).thenReturn(true);

    CarreraExisteException exception =
        assertThrows(CarreraExisteException.class, () -> carreraService.crearCarrera(dto));

    assertEquals("Ya existe una carrera con ese código.", exception.getMessage());
    verify(carreraRepository, never()).save(any());
  }

  @Test
  void crearCarreraConPlanDeEstudiosInexistenteLanzaExcepcion() {
    CarreraDTO dto = new CarreraDTO();
    dto.setCodigoDeCarrera("ING-SIS");
    dto.setCodigosPlanesDeEstudio(Arrays.asList("P-2025"));

    when(carreraRepository.existsByCodigoDeCarrera("ING-SIS")).thenReturn(false);
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios("P-2025")).thenReturn(Optional.empty());

    PlanDeEstudiosNoExisteException exception =
        assertThrows(PlanDeEstudiosNoExisteException.class, () -> carreraService.crearCarrera(dto));

    assertEquals("No se encontró el plan de estudios con código: P-2025", exception.getMessage());
    verify(carreraRepository, never()).save(any());
  }

  @Test
  void crearCarreraConCodigoNuevoGuardaCarreraCorrectamente() {
    CarreraDTO dto = new CarreraDTO();
    dto.setCodigoDeCarrera("ING-SIS");
    dto.setNombre("Ingeniería en Sistemas");
    dto.setTitulo("Ingeniero en Sistemas");
    dto.setIncumbencias("Desarrollo de software");
    dto.setCodigosPlanesDeEstudio(Arrays.asList("P-2025"));

    when(carreraRepository.existsByCodigoDeCarrera("ING-SIS")).thenReturn(false);
    
    PlanDeEstudios plan = new PlanDeEstudios(
        "P-2025",
        10,
        LocalDate.of(2025, 1, 1),
        new ArrayList<>(),
        LocalDate.of(2030, 12, 31)
    );
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios("P-2025")).thenReturn(Optional.of(plan));

    CarreraDTO resultado = carreraService.crearCarrera(dto);

    ArgumentCaptor<Carrera> carreraCaptor = ArgumentCaptor.forClass(Carrera.class);
    verify(carreraRepository).save(carreraCaptor.capture());

    Carrera carreraGuardada = carreraCaptor.getValue();
    assertEquals("ING-SIS", carreraGuardada.getCodigoDeCarrera());
    assertEquals("Ingeniería en Sistemas", carreraGuardada.getNombre());
    assertEquals("Ingeniero en Sistemas", carreraGuardada.getTitulo());
    assertEquals("Desarrollo de software", carreraGuardada.getIncumbencias());
    assertEquals(1, carreraGuardada.getPlanesDeEstudio().size());
    assertEquals("P-2025", carreraGuardada.getPlanesDeEstudio().get(0).getCodigoDePlanDeEstudios());

    assertEquals(dto, resultado);
  }

  @Test
  void crearCarreraSinPlanesDeEstudioGuardaCarreraCorrectamente() {
    CarreraDTO dto = new CarreraDTO();
    dto.setCodigoDeCarrera("MED");
    dto.setNombre("Medicina");
    dto.setTitulo("Médico");
    dto.setIncumbencias("Atención médica");
    dto.setCodigosPlanesDeEstudio(null);

    when(carreraRepository.existsByCodigoDeCarrera("MED")).thenReturn(false);

    CarreraDTO resultado = carreraService.crearCarrera(dto);

    ArgumentCaptor<Carrera> carreraCaptor = ArgumentCaptor.forClass(Carrera.class);
    verify(carreraRepository).save(carreraCaptor.capture());

    Carrera carreraGuardada = carreraCaptor.getValue();
    assertEquals("MED", carreraGuardada.getCodigoDeCarrera());
    assertEquals("Medicina", carreraGuardada.getNombre());
    assertEquals("Médico", carreraGuardada.getTitulo());
    assertEquals("Atención médica", carreraGuardada.getIncumbencias());
    assertTrue(carreraGuardada.getPlanesDeEstudio().isEmpty());

    assertEquals(dto, resultado);
  }

  @Test
  void obtenerCarreraPorCodigoLanzaExcepcionSiNoExiste() {
    String codigo = "ING-SIS";
    when(carreraRepository.findByCodigoDeCarrera(codigo)).thenReturn(Optional.empty());

    CarreraNoExisteException exception =
        assertThrows(CarreraNoExisteException.class, () -> carreraService.obtenerCarreraPorCodigo(codigo));

    assertEquals("No existe una carrera con ese código.", exception.getMessage());
  }

  @Test
  void obtenerCarreraPorCodigoDevuelveDTOCorrectoSiExiste() {
    String codigo = "ING-SIS";
    
    PlanDeEstudios plan1 = new PlanDeEstudios();
    plan1.setCodigoDePlanDeEstudios("P-2024");
    
    PlanDeEstudios plan2 = new PlanDeEstudios();
    plan2.setCodigoDePlanDeEstudios("P-2025");
    
    Carrera carrera = new Carrera();
    carrera.setCodigoDeCarrera(codigo);
    carrera.setNombre("Ingeniería en Sistemas");
    carrera.setTitulo("Ingeniero en Sistemas");
    carrera.setIncumbencias("Desarrollo de software");
    carrera.setPlanesDeEstudio(Arrays.asList(plan1, plan2));

    when(carreraRepository.findByCodigoDeCarrera(codigo)).thenReturn(Optional.of(carrera));

    CarreraDTO dto = carreraService.obtenerCarreraPorCodigo(codigo);

    assertNotNull(dto);
    assertEquals("ING-SIS", dto.getCodigoDeCarrera());
    assertEquals("Ingeniería en Sistemas", dto.getNombre());
    assertEquals("Ingeniero en Sistemas", dto.getTitulo());
    assertEquals("Desarrollo de software", dto.getIncumbencias());
    assertEquals(Arrays.asList("P-2024", "P-2025"), dto.getCodigosPlanesDeEstudio());
  }

  @Test
  void obtenerCarreraPorCodigoSinPlanesDeEstudioDevuelveDTOCorrect() {
    String codigo = "MED";
    
    Carrera carrera = new Carrera();
    carrera.setCodigoDeCarrera(codigo);
    carrera.setNombre("Medicina");
    carrera.setTitulo("Médico");
    carrera.setIncumbencias("Atención médica");
    carrera.setPlanesDeEstudio(null);

    when(carreraRepository.findByCodigoDeCarrera(codigo)).thenReturn(Optional.of(carrera));

    CarreraDTO dto = carreraService.obtenerCarreraPorCodigo(codigo);

    assertNotNull(dto);
    assertEquals("MED", dto.getCodigoDeCarrera());
    assertEquals("Medicina", dto.getNombre());
    assertEquals("Médico", dto.getTitulo());
    assertEquals("Atención médica", dto.getIncumbencias());
    assertNull(dto.getCodigosPlanesDeEstudio());
  }

  @Test
  void obtenerTodasLasCarrerasDevuelveListaDeDTOs() {
    PlanDeEstudios plan1 = new PlanDeEstudios();
    plan1.setCodigoDePlanDeEstudios("P-2025");
    
    Carrera carrera1 = new Carrera();
    carrera1.setCodigoDeCarrera("ING-SIS");
    carrera1.setNombre("Ingeniería en Sistemas");
    carrera1.setTitulo("Ingeniero en Sistemas");
    carrera1.setIncumbencias("Desarrollo de software");
    carrera1.setPlanesDeEstudio(Arrays.asList(plan1));

    Carrera carrera2 = new Carrera();
    carrera2.setCodigoDeCarrera("MED");
    carrera2.setNombre("Medicina");
    carrera2.setTitulo("Médico");
    carrera2.setIncumbencias("Atención médica");
    carrera2.setPlanesDeEstudio(null);

    when(carreraRepository.findAll()).thenReturn(Arrays.asList(carrera1, carrera2));

    List<CarreraDTO> resultado = carreraService.obtenerTodasLasCarreras();

    assertEquals(2, resultado.size());

    CarreraDTO dto1 = resultado.get(0);
    assertEquals("ING-SIS", dto1.getCodigoDeCarrera());
    assertEquals("Ingeniería en Sistemas", dto1.getNombre());
    assertEquals("Ingeniero en Sistemas", dto1.getTitulo());
    assertEquals("Desarrollo de software", dto1.getIncumbencias());
    assertEquals(Arrays.asList("P-2025"), dto1.getCodigosPlanesDeEstudio());

    CarreraDTO dto2 = resultado.get(1);
    assertEquals("MED", dto2.getCodigoDeCarrera());
    assertEquals("Medicina", dto2.getNombre());
    assertEquals("Médico", dto2.getTitulo());
    assertEquals("Atención médica", dto2.getIncumbencias());
    assertNull(dto2.getCodigosPlanesDeEstudio());
  }

  @Test
  void obtenerTodasLasCarrerasDevuelveListaVaciaSiNoHayCarreras() {
    when(carreraRepository.findAll()).thenReturn(List.of());

    List<CarreraDTO> resultado = carreraService.obtenerTodasLasCarreras();

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }

  @Test
  void crearCarreraConMultiplesPlanesDeEstudioGuardaCorrectamente() {
    CarreraDTO dto = new CarreraDTO();
    dto.setCodigoDeCarrera("ING-IND");
    dto.setNombre("Ingeniería Industrial");
    dto.setTitulo("Ingeniero Industrial");
    dto.setIncumbencias("Optimización de procesos");
    dto.setCodigosPlanesDeEstudio(Arrays.asList("P-2024", "P-2025"));

    when(carreraRepository.existsByCodigoDeCarrera("ING-IND")).thenReturn(false);
    
    PlanDeEstudios plan1 = new PlanDeEstudios();
    plan1.setCodigoDePlanDeEstudios("P-2024");
    
    PlanDeEstudios plan2 = new PlanDeEstudios();
    plan2.setCodigoDePlanDeEstudios("P-2025");
    
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios("P-2024")).thenReturn(Optional.of(plan1));
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios("P-2025")).thenReturn(Optional.of(plan2));

    CarreraDTO resultado = carreraService.crearCarrera(dto);

    ArgumentCaptor<Carrera> carreraCaptor = ArgumentCaptor.forClass(Carrera.class);
    verify(carreraRepository).save(carreraCaptor.capture());

    Carrera carreraGuardada = carreraCaptor.getValue();
    assertEquals(2, carreraGuardada.getPlanesDeEstudio().size());
    assertEquals("P-2024", carreraGuardada.getPlanesDeEstudio().get(0).getCodigoDePlanDeEstudios());
    assertEquals("P-2025", carreraGuardada.getPlanesDeEstudio().get(1).getCodigoDePlanDeEstudios());

    assertEquals(dto, resultado);
  }
}