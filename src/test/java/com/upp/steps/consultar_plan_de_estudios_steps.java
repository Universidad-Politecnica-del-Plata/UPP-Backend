package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.AlumnoDTO;
import com.upp.dto.CarreraDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Steps para el feature de consultar plan de estudios. Permite a un alumno ver su plan de estudios
 * asociado a su carrera.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class consultar_plan_de_estudios_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<PlanDeEstudiosResponseDTO> planResult;
  private PlanDeEstudiosResponseDTO planObtenido;

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Cuando("consulta su plan de estudios")
  public void consultaSuPlanDeEstudios() {
    // Obtenemos los datos del alumno logueado para conseguir su carrera
    FluxExchangeResult<AlumnoDTO> alumnoResult =
        webTestClient
            .get()
            .uri("/api/alumnos/me")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(AlumnoDTO.class);

    assertEquals(HttpStatus.OK, alumnoResult.getStatus(), "Debería poder obtener datos del alumno");
    AlumnoDTO alumno = alumnoResult.getResponseBody().blockFirst();
    assertNotNull(alumno, "El alumno debería existir");
    assertNotNull(alumno.getCodigosCarreras(), "El alumno debería tener carreras asociadas");
    assertFalse(alumno.getCodigosCarreras().isEmpty(), "El alumno debería tener al menos una carrera");

    // Obtenemos la carrera para conseguir el código del plan de estudios
    String codigoCarrera = alumno.getCodigosCarreras().get(0);
    FluxExchangeResult<CarreraDTO> carreraResult =
        webTestClient
            .get()
            .uri("/api/carreras/" + codigoCarrera)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CarreraDTO.class);

    assertEquals(HttpStatus.OK, carreraResult.getStatus(), "Debería poder obtener la carrera");
    CarreraDTO carrera = carreraResult.getResponseBody().blockFirst();
    assertNotNull(carrera, "La carrera debería existir");
    assertNotNull(
        carrera.getCodigosPlanesDeEstudio(),
        "La carrera debería tener planes de estudio asociados");
    assertFalse(
        carrera.getCodigosPlanesDeEstudio().isEmpty(),
        "La carrera debería tener al menos un plan de estudios");

    // Tomamos el primer plan de estudios de la carrera
    String codigoPlan = carrera.getCodigosPlanesDeEstudio().get(0);

    // Consultamos el plan de estudios
    planResult =
        webTestClient
            .get()
            .uri("/api/planDeEstudios/" + codigoPlan)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);

    if (planResult.getStatus() == HttpStatus.OK) {
      planObtenido = planResult.getResponseBody().blockFirst();
    }
  }

  @Entonces("se le informa que el codigo del plan es {string}")
  public void seLeInformaQueElCodigoDelPlanEs(String codigoPlan) {
    assertEquals(HttpStatus.OK, planResult.getStatus(), "La consulta debería ser exitosa");
    assertNotNull(planObtenido, "Se debería haber obtenido el plan de estudios");
    assertEquals(
        codigoPlan,
        planObtenido.getCodigoDePlanDeEstudios(),
        "El código del plan no coincide");
  }

  @Y("se le informa que la fecha de entrada en vigencia es {string}")
  public void seLeInformaQueLaFechaDeEntradaEnVigenciaEs(String fechaEsperada) {
    assertNotNull(planObtenido, "Se debería haber obtenido el plan de estudios");
    LocalDate fechaEsperadaParsed = LocalDate.parse(fechaEsperada, DATE_FORMATTER);
    assertEquals(
        fechaEsperadaParsed,
        planObtenido.getFechaEntradaEnVigencia(),
        "La fecha de entrada en vigencia no coincide");
  }

  @Y("se le informa que la fecha de vencimiento es {string}")
  public void seLeInformaQueLaFechaDeVencimientoEs(String fechaEsperada) {
    assertNotNull(planObtenido, "Se debería haber obtenido el plan de estudios");
    LocalDate fechaEsperadaParsed = LocalDate.parse(fechaEsperada, DATE_FORMATTER);
    assertEquals(
        fechaEsperadaParsed,
        planObtenido.getFechaVencimiento(),
        "La fecha de vencimiento no coincide");
  }

  @Y("se le informa que el codigo de carrera es {string}")
  public void seLeInformaQueElCodigoDeCarreraEs(String codigoCarrera) {
    assertNotNull(planObtenido, "Se debería haber obtenido el plan de estudios");
    assertEquals(
        codigoCarrera,
        planObtenido.getCodigoCarrera(),
        "El código de carrera no coincide");
  }

  @Entonces("se le informa que la materia {string} esta en el plan")
  public void seLeInformaQueLaMateriaEstaEnElPlan(String codigoMateria) {
    assertNotNull(planObtenido, "Se debería haber obtenido el plan de estudios");
    assertNotNull(
        planObtenido.getCodigosMaterias(), "El plan debería tener materias asociadas");
    assertTrue(
        planObtenido.getCodigosMaterias().contains(codigoMateria),
        "La materia " + codigoMateria + " debería estar en el plan");
  }

  @Y("se le informa que el plan tiene {int} creditos optativos")
  public void seLeInformaQueElPlanTieneCreditosOptativos(Integer creditosOptativos) {
    assertNotNull(planObtenido, "Se debería haber obtenido el plan de estudios");
    assertEquals(
        creditosOptativos,
        planObtenido.getCreditosElectivos(),
        "Los créditos optativos no coinciden");
  }
}
