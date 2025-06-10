package com.upp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.upp.dto.MateriaDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.model.Materia;
import com.upp.model.TipoMateria;
import com.upp.repository.MateriaRepository;
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
public class MateriaServiceTest {
  private MateriaRepository materiaRepository;
  private MateriaService materiaService;

  @BeforeEach
  void setUp() {
    materiaRepository = mock(MateriaRepository.class);
    materiaService = new MateriaService(materiaRepository);
  }

  @Test
  void crearMateriaConCodigoExistenteLanzaExcepcion() {
    MateriaDTO dto = new MateriaDTO();
    dto.setCodigoDeMateria("MAT101");

    // Mockeamos que el código ya existe
    when(materiaRepository.existsByCodigoDeMateria("MAT101")).thenReturn(true);

    MateriaExisteException exception =
        assertThrows(MateriaExisteException.class, () -> materiaService.crearMateria(dto));

    assertEquals("Ya existe una materia con ese codigo.", exception.getMessage());

    // Verificamos que no haya llamado a save
    verify(materiaRepository, never()).save(any());
  }

  @Test
  void crearMateriaConCodigoNuevoGuardaMateriaCorrectamente() {
    MateriaDTO dto = new MateriaDTO();
    dto.setCodigoDeMateria("MAT102");
    dto.setNombre("Matemática");
    dto.setContenidos("Álgebra, Geometría");
    dto.setCreditosQueOtorga(4);
    dto.setCreditosNecesarios(0);
    dto.setTipo(TipoMateria.OBLIGATORIA);
    dto.setCodigosCorrelativas(Arrays.asList("MAT100"));

    // Mock: el código NO existe
    when(materiaRepository.existsByCodigoDeMateria("MAT102")).thenReturn(false);

    // Mock: la correlativa MAT100 existe y se devuelve
    Materia correlativa = new Materia();
    correlativa.setCodigoDeMateria("MAT100");
    when(materiaRepository.findByCodigoDeMateria("MAT100")).thenReturn(Optional.of(correlativa));

    // Ejecutamos
    MateriaDTO resultado = materiaService.crearMateria(dto);

    // Capturamos el objeto Materia que se guardó
    ArgumentCaptor<Materia> materiaCaptor = ArgumentCaptor.forClass(Materia.class);
    verify(materiaRepository).save(materiaCaptor.capture());

    Materia materiaGuardada = materiaCaptor.getValue();

    assertEquals("MAT102", materiaGuardada.getCodigoDeMateria());
    assertEquals("Matemática", materiaGuardada.getNombre());
    assertEquals(1, materiaGuardada.getCorrelativas().size());
    assertEquals("MAT100", materiaGuardada.getCorrelativas().get(0).getCodigoDeMateria());

    // También podemos validar que el DTO devuelto sea igual al de entrada
    assertEquals(dto, resultado);
  }

  @Test
  void crearMateriaSinCorrelativasGuardaMateriaCorrectamente() {
    MateriaDTO dto = new MateriaDTO();
    dto.setCodigoDeMateria("MAT103");
    dto.setNombre("Física");
    dto.setCodigosCorrelativas(null);

    when(materiaRepository.existsByCodigoDeMateria("MAT103")).thenReturn(false);

    MateriaDTO resultado = materiaService.crearMateria(dto);

    verify(materiaRepository).save(any(Materia.class));

    assertEquals(dto, resultado);
  }

  @Test
  void modificarMateriaDeberiaActualizarMateriaSiExiste() {

    Materia existente =
        new Materia("MAT101", "Álgebra", "Contenidos viejos", 6, 0, TipoMateria.OBLIGATORIA);
    Materia correlativa = new Materia("MAT100", "Intro", "Cont", 6, 0, TipoMateria.OBLIGATORIA);

    MateriaDTO modificacion =
        new MateriaDTO(
            "MAT101",
            "Álgebra Lineal",
            "Contenidos nuevos",
            8,
            0,
            TipoMateria.OBLIGATORIA,
            Arrays.asList("MAT100"));

    when(materiaRepository.findByCodigoDeMateria("MAT101")).thenReturn(Optional.of(existente));
    when(materiaRepository.findByCodigoDeMateria("MAT100")).thenReturn(Optional.of(correlativa));

    MateriaDTO resultado = materiaService.modificarMateria("MAT101", modificacion);

    assertEquals("Álgebra Lineal", resultado.getNombre());
    ArgumentCaptor<Materia> captor = ArgumentCaptor.forClass(Materia.class);
    verify(materiaRepository).save(captor.capture());

    Materia guardada = captor.getValue();
    assertEquals("Álgebra Lineal", guardada.getNombre());
    assertEquals("Contenidos nuevos", guardada.getContenidos());
    assertEquals(8, guardada.getCreditosQueOtorga());
    assertEquals(1, guardada.getCorrelativas().size());
    assertEquals("MAT100", guardada.getCorrelativas().get(0).getCodigoDeMateria());
  }

  @Test
  void modificarMateriaDeberiaLanzarExcepcionSiNoExiste() {
    when(materiaRepository.findByCodigoDeMateria("INEXISTENTE")).thenReturn(Optional.empty());

    MateriaDTO dto =
        new MateriaDTO("INEXISTENTE", "Nombre", "Cont", 4, 0, TipoMateria.OPTATIVA, null);

    assertThrows(
        MateriaNoExisteException.class, () -> materiaService.modificarMateria("INEXISTENTE", dto));
  }

  @Test
  void eliminarMateriaLanzaExcepcionSiNoExisteMateriaConEseCodigo() {
    String codigo = "MAT101";
    when(materiaRepository.findByCodigoDeMateria(codigo)).thenReturn(Optional.empty());

    MateriaNoExisteException ex =
        assertThrows(MateriaNoExisteException.class, () -> materiaService.eliminarMateria(codigo));
    assertEquals("No existe una materia con ese codigo.", ex.getMessage());
  }

  @Test
  void eliminaMateriaSinSerCorrelativaDeOtra() {
    String codigo = "MAT101";
    Materia materia = new Materia();
    materia.setCodigoDeMateria(codigo);

    when(materiaRepository.findByCodigoDeMateria(codigo)).thenReturn(Optional.of(materia));
    when(materiaRepository.findAll()).thenReturn(List.of()); // No hay otras materias

    materiaService.eliminarMateria(codigo);

    verify(materiaRepository).delete(materia);
  }

  @Test
  void eliminaMateriaYLaQuitaDeOtrasCorrelativas() {
    String codigo = "MAT101";
    Materia materia = new Materia();
    materia.setCodigoDeMateria(codigo);

    Materia otra = new Materia();
    otra.setCodigoDeMateria("MAT102");
    otra.setCorrelativas(new ArrayList<>(List.of(materia)));

    when(materiaRepository.findByCodigoDeMateria(codigo)).thenReturn(Optional.of(materia));
    when(materiaRepository.findAll()).thenReturn(List.of(otra));

    materiaService.eliminarMateria(codigo);

    assertFalse(otra.getCorrelativas().contains(materia));
    verify(materiaRepository).save(otra);
    verify(materiaRepository).delete(materia);
  }

  @Test
  void obtenerMateriaPorCodigoLanzaExcepcionSiNoExiste() {
    String codigo = "MAT101";
    when(materiaRepository.findByCodigoDeMateria(codigo)).thenReturn(Optional.empty());

    MateriaNoExisteException exception =
        assertThrows(
            MateriaNoExisteException.class, () -> materiaService.obtenerMateriaPorCodigo(codigo));

    assertEquals("No existe una materia con ese codigo.", exception.getMessage());
  }

  @Test
  void obtenerMateriaPorCodigoDevuelveDTOCorrectoSiExiste() {

    String codigo = "MAT101";
    Materia materia = new Materia();
    materia.setCodigoDeMateria(codigo);
    materia.setNombre("Matemática");
    materia.setContenidos("Álgebra y análisis");
    materia.setCreditosQueOtorga(6);
    materia.setCreditosNecesarios(12);
    materia.setTipo(TipoMateria.OBLIGATORIA);

    Materia correlativa = new Materia();
    correlativa.setCodigoDeMateria("MAT001");

    materia.setCorrelativas(List.of(correlativa));

    when(materiaRepository.findByCodigoDeMateria(codigo)).thenReturn(Optional.of(materia));

    MateriaDTO dto = materiaService.obtenerMateriaPorCodigo(codigo);

    assertNotNull(dto);
    assertEquals("MAT101", dto.getCodigoDeMateria());
    assertEquals("Matemática", dto.getNombre());
    assertEquals("Álgebra y análisis", dto.getContenidos());
    assertEquals(6, dto.getCreditosQueOtorga());
    assertEquals(12, dto.getCreditosNecesarios());
    assertEquals(TipoMateria.OBLIGATORIA, dto.getTipo());
    assertEquals(List.of("MAT001"), dto.getCodigosCorrelativas());
  }

  @Test
  void obtenerTodasLasMateriasDevuelveListaDeDTOs() {
    Materia materia1 = new Materia();
    materia1.setCodigoDeMateria("MAT101");
    materia1.setNombre("Matemática");
    materia1.setContenidos("Álgebra");
    materia1.setCreditosQueOtorga(6);
    materia1.setCreditosNecesarios(12);
    materia1.setTipo(TipoMateria.OBLIGATORIA);
    materia1.setCorrelativas(List.of());

    Materia materia2 = new Materia();
    materia2.setCodigoDeMateria("FIS202");
    materia2.setNombre("Física");
    materia2.setContenidos("Mecánica");
    materia2.setCreditosQueOtorga(5);
    materia2.setCreditosNecesarios(10);
    materia2.setTipo(TipoMateria.OPTATIVA);
    materia2.setCorrelativas(new ArrayList<>(List.of(materia1)));

    when(materiaRepository.findAll()).thenReturn(List.of(materia1, materia2));

    List<MateriaDTO> resultado = materiaService.obtenerTodasLasMaterias();

    assertEquals(2, resultado.size());

    MateriaDTO dto1 = resultado.get(0);
    assertEquals("MAT101", dto1.getCodigoDeMateria());
    assertEquals("Matemática", dto1.getNombre());
    assertEquals("Álgebra", dto1.getContenidos());
    assertEquals(6, dto1.getCreditosQueOtorga());
    assertEquals(12, dto1.getCreditosNecesarios());
    assertEquals(TipoMateria.OBLIGATORIA, dto1.getTipo());

    MateriaDTO dto2 = resultado.get(1);
    assertEquals("FIS202", dto2.getCodigoDeMateria());
    assertEquals("Física", dto2.getNombre());
    assertEquals("Mecánica", dto2.getContenidos());
    assertEquals(5, dto2.getCreditosQueOtorga());
    assertEquals(10, dto2.getCreditosNecesarios());
    assertEquals(TipoMateria.OPTATIVA, dto2.getTipo());
  }

  @Test
  void obtenerTodasLasMateriasDevuelveListaVaciaSiNoHayMaterias() {
    when(materiaRepository.findAll()).thenReturn(List.of());

    List<MateriaDTO> resultado = materiaService.obtenerTodasLasMaterias();

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }
}
