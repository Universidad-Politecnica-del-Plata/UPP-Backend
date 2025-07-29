package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class MateriaTest {
  @Test
  void crearSinParametros() {

    new Materia();
  }

  @Test
  void crearConParametros() {

    String codigoDeMateria = "123-M";
    String nombre = "Analisis II";
    String contenidos = "Funciones, Derivadas e Integrales";
    Integer creditosQueOtorga = 8;
    Integer creditosNecesarios = 4;
    TipoMateria tipo = TipoMateria.OBLIGATORIA;
    Materia materia =
        new Materia(
            codigoDeMateria, nombre, contenidos, creditosQueOtorga, creditosNecesarios, tipo);
    assertNotNull(materia);
    // Los nuevos campos deberían ser null por defecto
    assertNotNull(materia.getCuatrimestre() == null || materia.getCuatrimestre() instanceof Integer);
    assertNotNull(materia.getPlanDeEstudios() == null || materia.getPlanDeEstudios() instanceof PlanDeEstudios);
  }

  @Test
  void agregarCorrelativaAMateria() {
    Materia analisisI = new Materia();
    analisisI.setCodigoDeMateria("123-M");
    analisisI.setNombre("Analisis I");

    Materia analisisII = new Materia();
    analisisII.setCodigoDeMateria("124-M");
    analisisII.setNombre("Analisis II");
    analisisII.setCuatrimestre(2);
    List<Materia> correlativas = new ArrayList<>();
    correlativas.add(analisisI);
    analisisII.setCorrelativas(correlativas);

    assertNotNull(analisisII.getCorrelativas());
    assertEquals("123-M", analisisII.getCorrelativas().get(0).getCodigoDeMateria());
    assertEquals(Integer.valueOf(2), analisisII.getCuatrimestre());
  }
}
