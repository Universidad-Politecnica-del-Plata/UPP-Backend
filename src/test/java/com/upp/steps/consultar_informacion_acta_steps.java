package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.ActaDTO;
import com.upp.dto.NotaDTO;
import com.upp.steps.shared.AuthHelper;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Steps para el feature de consultar información de acta. Permite a un docente consultar las actas
 * de un curso y verificar las notas cargadas.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class consultar_informacion_acta_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  @Autowired private AuthHelper authHelper;

  private FluxExchangeResult<ActaDTO> consultaActasResult;
  private List<ActaDTO> actasObtenidas;
  private ActaDTO actaActual;

  @Cuando("el docente consulta las actas del curso {string}")
  public void elDocenteConsultaLasActasDelCurso(String codigoCurso) {
    authHelper.loginDocente();

    consultaActasResult =
        webTestClient
            .get()
            .uri("/api/actas/curso/" + codigoCurso)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(ActaDTO.class);

    if (consultaActasResult.getStatus() == HttpStatus.OK) {
      actasObtenidas = consultaActasResult.getResponseBody().collectList().block();
      // Guardamos la primera acta para las verificaciones
      if (actasObtenidas != null && !actasObtenidas.isEmpty()) {
        actaActual = actasObtenidas.get(0);
      }
    }
  }

  @Entonces("se le informa que el acta tiene numero correlativo")
  public void seLeInformaQueElActaTieneNumeroCorrelativo() {
    assertEquals(
        HttpStatus.OK,
        consultaActasResult.getStatus(),
        "La consulta de actas debería ser exitosa");
    assertNotNull(actaActual, "Debería existir al menos un acta");
    assertNotNull(
        actaActual.getNumeroCorrelativo(), "El acta debería tener un número correlativo");
  }

  @Entonces("se le informa que el tipo de acta es {string}")
  public void seLeInformaQueElTipoDeActaEs(String tipoActa) {
    assertNotNull(actaActual, "Debería existir al menos un acta");
    assertEquals(
        tipoActa.toUpperCase(),
        actaActual.getTipoDeActa().name(),
        "El tipo de acta no coincide");
  }

  @Entonces("se le informa que el estado del acta es {string}")
  public void seLeInformaQueElEstadoDelActaEs(String estado) {
    assertNotNull(actaActual, "Debería existir al menos un acta");
    assertEquals(estado.toUpperCase(), actaActual.getEstado().name(), "El estado no coincide");
  }

  @Entonces("se le informa que el codigo del curso es {string}")
  public void seLeInformaQueElCodigoDelCursoEs(String codigoCurso) {
    assertNotNull(actaActual, "Debería existir al menos un acta");
    assertEquals(codigoCurso, actaActual.getCodigoCurso(), "El código del curso no coincide");
  }

  @Entonces("se le informa una nota con nombre {string}, apellido {string} y valor {int}")
  public void seLeInformaUnaNotaConNombreApellidoYValor(
      String nombre, String apellido, Integer valor) {
    assertNotNull(actaActual, "Debería existir al menos un acta");
    assertNotNull(actaActual.getNotas(), "El acta debería tener notas");
    assertFalse(actaActual.getNotas().isEmpty(), "El acta debería tener al menos una nota");

    boolean notaEncontrada =
        actaActual.getNotas().stream()
            .anyMatch(
                nota ->
                    nombre.equals(nota.getNombreAlumno())
                        && apellido.equals(nota.getApellidoAlumno())
                        && valor.equals(nota.getValor()));

    assertTrue(
        notaEncontrada,
        String.format(
            "Debería encontrarse una nota con nombre %s, apellido %s y valor %d",
            nombre, apellido, valor));
  }

  @Entonces("se le informa que no hay actas para el curso")
  public void seLeInformaQueNoHayActasParaElCurso() {
    assertEquals(
        HttpStatus.OK,
        consultaActasResult.getStatus(),
        "La consulta de actas debería ser exitosa");
    assertTrue(
        actasObtenidas == null || actasObtenidas.isEmpty(),
        "No debería haber actas para el curso");
  }

  @Entonces("se le informa que el curso buscado no existe")
  public void seLeInformaQueElCursoNoExiste() {
    assertTrue(
        consultaActasResult.getStatus().is4xxClientError(),
        "Debería retornar un error 4xx cuando el curso no existe");
  }
}
