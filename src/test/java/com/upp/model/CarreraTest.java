package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CarreraTest {
  @Test
  void crearSinParametros() {
    new Carrera();
  }

  @Test
  void crearConParametros() {
    String codigoDeCarrera = "ING-SIS";
    String nombre = "Ingeniería en Sistemas";
    String titulo = "Ingeniero en Sistemas";
    String incumbencias = "Desarrollo de software, análisis de sistemas";
    
    List<PlanDeEstudios> planesDeEstudio = new ArrayList<>();
    PlanDeEstudios plan = new PlanDeEstudios(
        "P-2025",
        10,
        LocalDate.of(2025, 1, 1),
        new ArrayList<>(),
        LocalDate.of(2030, 12, 31)
    );
    planesDeEstudio.add(plan);
    
    Carrera carrera = new Carrera(
        codigoDeCarrera,
        nombre,
        titulo,
        incumbencias,
        planesDeEstudio
    );
    
    assertNotNull(carrera);
    assertEquals("ING-SIS", carrera.getCodigoDeCarrera());
    assertEquals("Ingeniería en Sistemas", carrera.getNombre());
    assertEquals("Ingeniero en Sistemas", carrera.getTitulo());
    assertEquals("Desarrollo de software, análisis de sistemas", carrera.getIncumbencias());
    assertEquals(1, carrera.getPlanesDeEstudio().size());
    assertEquals("P-2025", carrera.getPlanesDeEstudio().get(0).getCodigoDePlanDeEstudios());
  }

  @Test
  void crearConParametrosSinPlanesDeEstudio() {
    String codigoDeCarrera = "MED";
    String nombre = "Medicina";
    String titulo = "Médico";
    String incumbencias = "Atención médica";
    
    Carrera carrera = new Carrera(
        codigoDeCarrera,
        nombre,
        titulo,
        incumbencias,
        null
    );
    
    assertNotNull(carrera);
    assertEquals("MED", carrera.getCodigoDeCarrera());
    assertEquals("Medicina", carrera.getNombre());
    assertEquals("Médico", carrera.getTitulo());
    assertEquals("Atención médica", carrera.getIncumbencias());
    assertNull(carrera.getPlanesDeEstudio());
  }

  @Test
  void agregarPlanesDeEstudioACarrera() {
    Carrera carrera = new Carrera();
    carrera.setCodigoDeCarrera("ING-IND");
    carrera.setNombre("Ingeniería Industrial");
    carrera.setTitulo("Ingeniero Industrial");
    carrera.setIncumbencias("Optimización de procesos");
    
    PlanDeEstudios plan1 = new PlanDeEstudios(
        "P-2024",
        8,
        LocalDate.of(2024, 1, 1),
        new ArrayList<>(),
        LocalDate.of(2029, 12, 31)
    );
    
    PlanDeEstudios plan2 = new PlanDeEstudios(
        "P-2025",
        10,
        LocalDate.of(2025, 1, 1),
        new ArrayList<>(),
        LocalDate.of(2030, 12, 31)
    );
    
    List<PlanDeEstudios> planes = new ArrayList<>();
    planes.add(plan1);
    planes.add(plan2);
    
    carrera.setPlanesDeEstudio(planes);
    
    assertNotNull(carrera.getPlanesDeEstudio());
    assertEquals(2, carrera.getPlanesDeEstudio().size());
    assertEquals("P-2024", carrera.getPlanesDeEstudio().get(0).getCodigoDePlanDeEstudios());
    assertEquals("P-2025", carrera.getPlanesDeEstudio().get(1).getCodigoDePlanDeEstudios());
  }

  @Test
  void modificarPlanesDeEstudioDeCarrera() {
    Carrera carrera = new Carrera();
    carrera.setCodigoDeCarrera("ARQ");
    carrera.setNombre("Arquitectura");
    
    PlanDeEstudios planInicial = new PlanDeEstudios(
        "P-2023",
        6,
        LocalDate.of(2023, 1, 1),
        new ArrayList<>(),
        LocalDate.of(2028, 12, 31)
    );
    
    List<PlanDeEstudios> planesIniciales = new ArrayList<>();
    planesIniciales.add(planInicial);
    carrera.setPlanesDeEstudio(planesIniciales);
    
    assertEquals(1, carrera.getPlanesDeEstudio().size());
    assertEquals("P-2023", carrera.getPlanesDeEstudio().get(0).getCodigoDePlanDeEstudios());
    
    PlanDeEstudios planNuevo = new PlanDeEstudios(
        "P-2025",
        12,
        LocalDate.of(2025, 1, 1),
        new ArrayList<>(),
        LocalDate.of(2030, 12, 31)
    );
    
    List<PlanDeEstudios> planesNuevos = new ArrayList<>();
    planesNuevos.add(planNuevo);
    carrera.setPlanesDeEstudio(planesNuevos);
    
    assertEquals(1, carrera.getPlanesDeEstudio().size());
    assertEquals("P-2025", carrera.getPlanesDeEstudio().get(0).getCodigoDePlanDeEstudios());
  }
}