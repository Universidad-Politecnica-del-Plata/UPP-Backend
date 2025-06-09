package com.upp.service;

import com.upp.dto.MateriaDTO;
import com.upp.exception.MateriaExisteException;
import com.upp.exception.MateriaNoExisteException;
import com.upp.model.Materia;
import com.upp.model.TipoMateria;
import com.upp.repository.MateriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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

        MateriaExisteException exception = assertThrows(MateriaExisteException.class,
                () -> materiaService.crearMateria(dto));

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

        Materia existente = new Materia("MAT101", "Álgebra", "Contenidos viejos", 6, 0, TipoMateria.OBLIGATORIA);
        Materia correlativa = new Materia("MAT100", "Intro", "Cont", 6, 0, TipoMateria.OBLIGATORIA);

        MateriaDTO modificacion = new MateriaDTO(
                "MAT101", "Álgebra Lineal", "Contenidos nuevos", 8, 0,
                TipoMateria.OBLIGATORIA, Arrays.asList("MAT100")
        );

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

        MateriaDTO dto = new MateriaDTO("INEXISTENTE", "Nombre", "Cont", 4, 0, TipoMateria.OPTATIVA, null);

        assertThrows(MateriaNoExisteException.class, () ->
                materiaService.modificarMateria("INEXISTENTE", dto));
    }
}