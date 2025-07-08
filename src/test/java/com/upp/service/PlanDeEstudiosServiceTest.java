package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.exception.PlanDeEstudiosExisteException;
import com.upp.exception.PlanDeEstudiosNoExisteException;
import com.upp.model.Materia;
import com.upp.model.PlanDeEstudios;
import com.upp.model.TipoMateria;
import com.upp.repository.MateriaRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PlanDeEstudiosServiceTest {
  private MateriaRepository materiaRepository;
  private PlanDeEstudiosRepository planDeEstudiosRepository;
  private PlanDeEstudiosService planDeEstudiosService;

  @BeforeEach
  void setUp() {
    materiaRepository = mock(MateriaRepository.class);
    planDeEstudiosRepository = mock(PlanDeEstudiosRepository.class);
    planDeEstudiosService = new PlanDeEstudiosService(planDeEstudiosRepository, materiaRepository);
  }

  @Test
  void crearPlanDeEstudiosConCodigoExistenteLanzaExcepcion() {
    PlanDeEstudiosRequestDTO dto = new PlanDeEstudiosRequestDTO();
    dto.setCodigoDePlanDeEstudios("P-2025");

    // Mockeamos que el código ya existe
    when(planDeEstudiosRepository.existsByCodigoDePlanDeEstudios("P-2025")).thenReturn(true);

    PlanDeEstudiosExisteException exception =
        assertThrows(
            PlanDeEstudiosExisteException.class,
            () -> planDeEstudiosService.crearPlanDeEstudios(dto));

    assertEquals("Ya existe un plan de estudios con ese codigo.", exception.getMessage());

    // Verificamos que no haya llamado a save
    verify(planDeEstudiosRepository, never()).save(any());
  }

  @Test
  void crearPlanDeEstudiosConCodigoNuevoGuardaPlanDeEstudiosCorrectamente() {
    PlanDeEstudiosRequestDTO dto = new PlanDeEstudiosRequestDTO();
    dto.setCodigoDePlanDeEstudios("P-2025");
    dto.setCreditosElectivos(10);
    dto.setFechaVencimiento(LocalDate.of(2035, 1, 1));
    dto.setFechaEntradaEnVigencia(LocalDate.of(1990, 1, 1));
    dto.setCodigosMaterias(Arrays.asList("MAT100"));

    // Mock: el código NO existe
    when(planDeEstudiosRepository.existsByCodigoDePlanDeEstudios("P-2025")).thenReturn(false);

    // Mock: la materia MAT100 existe y se devuelve
    Materia materia = new Materia();
    materia.setCodigoDeMateria("MAT100");
    materia.setCreditosQueOtorga(6);
    materia.setTipo(TipoMateria.OBLIGATORIA);
    when(materiaRepository.findByCodigoDeMateria("MAT100")).thenReturn(Optional.of(materia));

    // Ejecutamos
    PlanDeEstudiosResponseDTO resultado = planDeEstudiosService.crearPlanDeEstudios(dto);

    // Capturamos el objeto Plan de Estudios que se guardó
    ArgumentCaptor<PlanDeEstudios> planDeEstudiosCaptor =
        ArgumentCaptor.forClass(PlanDeEstudios.class);
    verify(planDeEstudiosRepository).save(planDeEstudiosCaptor.capture());

    PlanDeEstudios planDeEstudiosGuardado = planDeEstudiosCaptor.getValue();

    assertEquals("P-2025", planDeEstudiosGuardado.getCodigoDePlanDeEstudios());
    assertEquals(6, planDeEstudiosGuardado.getCreditosObligatorios());
    assertEquals("MAT100", planDeEstudiosGuardado.getMaterias().get(0).getCodigoDeMateria());
  }

  @Test
  void obtenerPlanDeEstudiosPorCodigoLanzaExcepcionSiNoExiste() {
    String codigo = "P-2025";
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.empty());

    PlanDeEstudiosNoExisteException exception =
        assertThrows(
            PlanDeEstudiosNoExisteException.class,
            () -> planDeEstudiosService.obtenerPlanDeEstudiosPorCodigo(codigo));

    assertEquals("No existe un plan de estudios con ese codigo.", exception.getMessage());
  }

  @Test
  void obtenerPlanDeEstudiosPorCodigoDevuelveDTOCorrectoSiExiste() {
    Materia materia = new Materia();
    materia.setCodigoDeMateria("MAT100");
    materia.setCreditosQueOtorga(6);
    materia.setTipo(TipoMateria.OBLIGATORIA);
    ArrayList<Materia> materias = new ArrayList<>();
    materias.add(materia);

    String codigo = "P-2025";
    PlanDeEstudios planDeEstudios = new PlanDeEstudios();
    planDeEstudios.setCodigoDePlanDeEstudios(codigo);
    planDeEstudios.setCreditosElectivos(1);
    planDeEstudios.setFechaVencimiento(LocalDate.of(2035, 1, 1));
    planDeEstudios.setFechaEntradaEnVigencia(LocalDate.of(1990, 1, 1));
    planDeEstudios.setMaterias(materias);

    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.of(planDeEstudios));
    PlanDeEstudiosResponseDTO dto = planDeEstudiosService.obtenerPlanDeEstudiosPorCodigo(codigo);

    assertNotNull(dto);
    assertEquals("P-2025", dto.getCodigoDePlanDeEstudios());
  }
}
