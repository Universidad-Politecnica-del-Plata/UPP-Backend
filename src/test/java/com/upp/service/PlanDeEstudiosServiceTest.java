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
import java.util.List;
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

  @Test
  void obtenerTodosLosPlanDeEstudiosLosDevuelveComoListaDeDTOs() {
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

    PlanDeEstudios planDeEstudios =
        new PlanDeEstudios(
            dto.getCodigoDePlanDeEstudios(),
            dto.getCreditosElectivos(),
            dto.getFechaEntradaEnVigencia(),
            List.of(materia),
            dto.getFechaVencimiento());

    // Ejecutamos
    PlanDeEstudiosResponseDTO resultado = planDeEstudiosService.crearPlanDeEstudios(dto);

    PlanDeEstudiosRequestDTO dto2 = new PlanDeEstudiosRequestDTO();
    dto2.setCodigoDePlanDeEstudios("P-2026");
    dto2.setCreditosElectivos(10);
    dto2.setFechaVencimiento(LocalDate.of(2035, 1, 1));
    dto2.setFechaEntradaEnVigencia(LocalDate.of(1990, 1, 1));
    dto2.setCodigosMaterias(Arrays.asList("MAT101", "MAT100"));

    // Mock: el código NO existe
    when(planDeEstudiosRepository.existsByCodigoDePlanDeEstudios("P-2026")).thenReturn(false);

    // Mock: la materia MAT101 existe y se devuelve
    Materia materia2 = new Materia();
    materia2.setCodigoDeMateria("MAT101");
    materia2.setCreditosQueOtorga(6);
    materia2.setTipo(TipoMateria.OBLIGATORIA);
    when(materiaRepository.findByCodigoDeMateria("MAT101")).thenReturn(Optional.of(materia2));

    PlanDeEstudios planDeEstudios2 =
        new PlanDeEstudios(
            dto2.getCodigoDePlanDeEstudios(),
            dto2.getCreditosElectivos(),
            dto2.getFechaEntradaEnVigencia(),
            Arrays.asList(materia, materia2),
            dto2.getFechaVencimiento());

    // Ejecutamos
    PlanDeEstudiosResponseDTO resultado2 = planDeEstudiosService.crearPlanDeEstudios(dto2);

    // Validamos
    when(planDeEstudiosRepository.findAll())
        .thenReturn(Arrays.asList(planDeEstudios, planDeEstudios2));

    List<PlanDeEstudiosResponseDTO> planesGuardados =
        planDeEstudiosService.obtenerTodosLosPlanesDeEstudios();
    assertEquals(2, planesGuardados.size());

    PlanDeEstudiosResponseDTO plan1 = planesGuardados.get(0);
    PlanDeEstudiosResponseDTO plan2 = planesGuardados.get(1);

    assertEquals("P-2025", plan1.getCodigoDePlanDeEstudios());
    assertEquals("P-2026", plan2.getCodigoDePlanDeEstudios());
    assertEquals(6, plan1.getCreditosObligatorios());
    assertEquals(12, plan2.getCreditosObligatorios());
  }

  @Test
  void obtenerTodosLosPlanesDevuelveListaVaciaSiNoHayPlanes() {
    when(planDeEstudiosRepository.findAll()).thenReturn(List.of());

    List<PlanDeEstudiosResponseDTO> resultado =
        planDeEstudiosService.obtenerTodosLosPlanesDeEstudios();
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }

  @Test
  void eliminarPlanLanzaExcepcionSiNoExistePlanConEseCodigo() {
    String codigo = "P-2025";
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.empty());

    PlanDeEstudiosNoExisteException ex =
        assertThrows(
            PlanDeEstudiosNoExisteException.class,
            () -> planDeEstudiosService.eliminarPlanDeEstudios(codigo));
    assertEquals("No existe un plan de estudios con ese codigo.", ex.getMessage());
  }

  @Test
  void eliminaPlanDeEstudios() {
    String codigo = "P-2025";
    PlanDeEstudios planDeEstudios = new PlanDeEstudios();
    planDeEstudios.setCodigoDePlanDeEstudios(codigo);

    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.of(planDeEstudios));
    planDeEstudiosService.eliminarPlanDeEstudios(codigo);
    verify(planDeEstudiosRepository).delete(planDeEstudios);
  }

  @Test
  void modificarPlanDeEstudiosLanzaExcepcionSiNoExiste() {
    String codigo = "P-2025";
    PlanDeEstudiosRequestDTO dto = new PlanDeEstudiosRequestDTO();
    dto.setCodigoDePlanDeEstudios(codigo);
    dto.setCreditosElectivos(15);
    dto.setFechaEntradaEnVigencia(LocalDate.of(2025, 2, 1));
    dto.setFechaVencimiento(LocalDate.of(2035, 12, 31));
    dto.setCodigosMaterias(Arrays.asList("MAT100", "MAT101"));

    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.empty());

    PlanDeEstudiosNoExisteException exception =
        assertThrows(
            PlanDeEstudiosNoExisteException.class,
            () -> planDeEstudiosService.modificarPlanDeEstudios(codigo, dto));

    assertEquals("No existe un plan de estudios con ese codigo.", exception.getMessage());
    verify(planDeEstudiosRepository, never()).save(any());
  }

  @Test
  void modificarPlanDeEstudiosActualizaCorrectamenteTodosLosCampos() {
    String codigo = "P-2025";
    
    // Plan de estudios existente
    Materia materiaOriginal = new Materia();
    materiaOriginal.setCodigoDeMateria("MAT100");
    materiaOriginal.setCreditosQueOtorga(4);
    materiaOriginal.setTipo(TipoMateria.OBLIGATORIA);
    
    PlanDeEstudios planExistente = new PlanDeEstudios();
    planExistente.setCodigoDePlanDeEstudios(codigo);
    planExistente.setCreditosElectivos(10);
    planExistente.setFechaEntradaEnVigencia(LocalDate.of(2025, 1, 1));
    planExistente.setFechaVencimiento(LocalDate.of(2035, 1, 1));
    planExistente.setMaterias(List.of(materiaOriginal));

    // DTO con nuevos datos
    PlanDeEstudiosRequestDTO dto = new PlanDeEstudiosRequestDTO();
    dto.setCodigoDePlanDeEstudios(codigo);
    dto.setCreditosElectivos(20);
    dto.setFechaEntradaEnVigencia(LocalDate.of(2025, 3, 1));
    dto.setFechaVencimiento(LocalDate.of(2036, 12, 31));
    dto.setCodigosMaterias(Arrays.asList("MAT101", "MAT102"));

    // Nuevas materias
    Materia materia1 = new Materia();
    materia1.setCodigoDeMateria("MAT101");
    materia1.setCreditosQueOtorga(6);
    materia1.setTipo(TipoMateria.OBLIGATORIA);
    
    Materia materia2 = new Materia();
    materia2.setCodigoDeMateria("MAT102");
    materia2.setCreditosQueOtorga(5);
    materia2.setTipo(TipoMateria.OPTATIVA);

    // Mocks
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.of(planExistente));
    when(materiaRepository.findByCodigoDeMateria("MAT101"))
        .thenReturn(Optional.of(materia1));
    when(materiaRepository.findByCodigoDeMateria("MAT102"))
        .thenReturn(Optional.of(materia2));

    // Ejecutar
    PlanDeEstudiosResponseDTO resultado = 
        planDeEstudiosService.modificarPlanDeEstudios(codigo, dto);

    // Verificar que se guardó el plan modificado
    ArgumentCaptor<PlanDeEstudios> planCaptor = ArgumentCaptor.forClass(PlanDeEstudios.class);
    verify(planDeEstudiosRepository).save(planCaptor.capture());
    
    PlanDeEstudios planGuardado = planCaptor.getValue();
    
    // Verificar que todos los campos se actualizaron
    assertEquals(codigo, planGuardado.getCodigoDePlanDeEstudios());
    assertEquals(20, planGuardado.getCreditosElectivos());
    assertEquals(LocalDate.of(2025, 3, 1), planGuardado.getFechaEntradaEnVigencia());
    assertEquals(LocalDate.of(2036, 12, 31), planGuardado.getFechaVencimiento());
    assertEquals(2, planGuardado.getMaterias().size());
    assertEquals(6, planGuardado.getCreditosObligatorios()); // Solo MAT101 es obligatoria
    
    // Verificar el DTO de respuesta
    assertNotNull(resultado);
    assertEquals(codigo, resultado.getCodigoDePlanDeEstudios());
    assertEquals(20, resultado.getCreditosElectivos());
    assertEquals(LocalDate.of(2025, 3, 1), resultado.getFechaEntradaEnVigencia());
    assertEquals(LocalDate.of(2036, 12, 31), resultado.getFechaVencimiento());
    assertEquals(2, resultado.getCodigosMaterias().size());
    assertTrue(resultado.getCodigosMaterias().contains("MAT101"));
    assertTrue(resultado.getCodigosMaterias().contains("MAT102"));
    assertEquals(6, resultado.getCreditosObligatorios());
  }

  @Test
  void modificarPlanDeEstudiosConMateriasVacias() {
    String codigo = "P-2025";
    
    // Plan de estudios existente
    Materia materiaOriginal = new Materia();
    materiaOriginal.setCodigoDeMateria("MAT100");
    materiaOriginal.setCreditosQueOtorga(4);
    materiaOriginal.setTipo(TipoMateria.OBLIGATORIA);
    
    PlanDeEstudios planExistente = new PlanDeEstudios();
    planExistente.setCodigoDePlanDeEstudios(codigo);
    planExistente.setCreditosElectivos(10);
    planExistente.setFechaEntradaEnVigencia(LocalDate.of(2025, 1, 1));
    planExistente.setFechaVencimiento(LocalDate.of(2035, 1, 1));
    planExistente.setMaterias(List.of(materiaOriginal));

    // DTO sin materias
    PlanDeEstudiosRequestDTO dto = new PlanDeEstudiosRequestDTO();
    dto.setCodigoDePlanDeEstudios(codigo);
    dto.setCreditosElectivos(15);
    dto.setFechaEntradaEnVigencia(LocalDate.of(2025, 2, 1));
    dto.setFechaVencimiento(LocalDate.of(2035, 12, 31));
    dto.setCodigosMaterias(List.of()); // Lista vacía

    // Mocks
    when(planDeEstudiosRepository.findByCodigoDePlanDeEstudios(codigo))
        .thenReturn(Optional.of(planExistente));

    // Ejecutar
    PlanDeEstudiosResponseDTO resultado = 
        planDeEstudiosService.modificarPlanDeEstudios(codigo, dto);

    // Verificar
    ArgumentCaptor<PlanDeEstudios> planCaptor = ArgumentCaptor.forClass(PlanDeEstudios.class);
    verify(planDeEstudiosRepository).save(planCaptor.capture());
    
    PlanDeEstudios planGuardado = planCaptor.getValue();
    
    assertEquals(codigo, planGuardado.getCodigoDePlanDeEstudios());
    assertEquals(15, planGuardado.getCreditosElectivos());
    assertEquals(0, planGuardado.getMaterias().size());
    assertEquals(0, planGuardado.getCreditosObligatorios());
    
    assertNotNull(resultado);
    assertEquals(0, resultado.getCodigosMaterias().size());
    assertEquals(0, resultado.getCreditosObligatorios());
  }
}
