package com.upp.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
            new Materia(
                    "123-M", "Analisis II", "Funciones, Derivadas e Integrales", 8, 4, tipo);
    List<Materia> materias = new ArrayList<>();
    materias.add(materia);
    PlanDeEstudios planDeEstudios = new PlanDeEstudios(codigoDePlanDeEstudios,creditosElectivos,fechaEntradaEnVigencia,materias,fechaVencimiento);
    assertNotNull(planDeEstudios);
  }


  @Test
  void agregarCorrelativaAMateria() {
    Materia analisisI = new Materia();
    analisisI.setCodigoDeMateria("123-M");
    analisisI.setNombre("Analisis I");

    Materia analisisII = new Materia();
    analisisII.setCodigoDeMateria("124-M");
    analisisII.setNombre("Analisis II");
    List<Materia> correlativas = new ArrayList<>();
    correlativas.add(analisisI);
    analisisII.setCorrelativas(correlativas);

    assertNotNull(analisisII.getCorrelativas());
    assertEquals("123-M", analisisII.getCorrelativas().get(0).getCodigoDeMateria());
  }
}
