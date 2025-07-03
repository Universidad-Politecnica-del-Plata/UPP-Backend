package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PlanDeEstudiosTest {
  @Test
  void crearSinParametros() {

    new PlanDeEstudios();
  }

  @Test
  void crearConParametros() {

    String codigoDePlanDeEstudios = "P-2025";
    Integer creditosElectivos = 10;
    LocalDate fechaEntradaEnVigencia = LocalDate.of(1990, 1, 1);
    LocalDate fechaVencimiento = LocalDate.of(2035, 1, 1);

    TipoMateria tipo = TipoMateria.OBLIGATORIA;
    Materia materia =
        new Materia("123-M", "Analisis II", "Funciones, Derivadas e Integrales", 8, 4, tipo);
    List<Materia> materias = new ArrayList<>();
    materias.add(materia);
    PlanDeEstudios planDeEstudios =
        new PlanDeEstudios(
            codigoDePlanDeEstudios,
            creditosElectivos,
            fechaEntradaEnVigencia,
            materias,
            fechaVencimiento);
    assertNotNull(planDeEstudios);
    assertEquals(8, planDeEstudios.getCreditosObligatorios());
    assertEquals("123-M", planDeEstudios.getMaterias().get(0).getCodigoDeMateria());

  }

  @Test
  void agregarMateriaActualizaCreditosObligatorios() {
    String codigoDePlanDeEstudios = "P-2025";
    Integer creditosElectivos = 10;
    LocalDate fechaEntradaEnVigencia = LocalDate.of(1990, 1, 1);
    LocalDate fechaVencimiento = LocalDate.of(2035, 1, 1);

    TipoMateria tipo = TipoMateria.OBLIGATORIA;
    Materia materia =
        new Materia("123-M", "Analisis II", "Funciones, Derivadas e Integrales", 8, 4, tipo);
    List<Materia> materias = new ArrayList<>();
    materias.add(materia);
    PlanDeEstudios planDeEstudios =
        new PlanDeEstudios(
            codigoDePlanDeEstudios,
            creditosElectivos,
            fechaEntradaEnVigencia,
            materias,
            fechaVencimiento);
    assertEquals(8, planDeEstudios.getCreditosObligatorios());
    Materia materia2 = new Materia("124-M", "Algebra II", "Autovalores y Autovectores", 6, 4, tipo);
    materias.add(materia2);
    planDeEstudios.setMaterias(materias);
    assertEquals(14, planDeEstudios.getCreditosObligatorios());
  }
}
