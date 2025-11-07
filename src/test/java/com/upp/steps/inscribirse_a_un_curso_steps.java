package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.InscripcionDTO;
import com.upp.repository.*;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class inscribirse_a_un_curso_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<InscripcionDTO> inscripcionResult;
  private FluxExchangeResult<List> consultaResult;
  private FluxExchangeResult<Map> eliminarResult;
  private Long codigoInscripcionGuardado;
  

  @Dado("que hay un alumno logueado con username {string}, password {string}")
  public void crearAlumnoLogueado(String username, String password) {
    // Login del alumno para obtener token
    Map<String, String> loginData =
        Map.of(
            "username", username,
            "password", password);

    String token =
        webTestClient
            .post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginData)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Map.class)
            .getResponseBody()
            .blockFirst()
            .get("token")
            .toString();

    tokenHolder.setToken(token);
  }

  @Cuando("el alumno se inscribe al curso {string} en el cuatrimestre actual")
  public void elAlumnoSeInscribeAlCurso(String codigoCurso) {

    Map<String, String> inscripcionData = Map.of("codigoCurso", codigoCurso);

    inscripcionResult =
        webTestClient
            .post()
            .uri("/api/inscripciones")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(inscripcionData)
            .exchange()
            .returnResult(InscripcionDTO.class);
  }

  @Entonces("la inscripción se realiza exitosamente")
  public void laInscripcionSeRealizaExitosamente() {
    assertEquals(HttpStatus.CREATED, inscripcionResult.getStatus());
  }

  @Entonces("no se puede realizar la inscripción exitosamente")
  public void noSePuedeRealizarLaInscripcionExitosamente() {
    assertTrue(
        inscripcionResult.getStatus().is4xxClientError()
            || inscripcionResult.getStatus().is5xxServerError());
  }

  @Y("se le informa que el curso no existe")
  public void seLaInformaQueElCursoNoExiste() {
    // El mensaje específico depende del manejo de errores global
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Y("se le informa que el cuatrimestre no existe")
  public void seLaInformaQueElCuatrimestreNoExiste() {
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Dado("que el alumno ya está inscrito al curso {string} en el cuatrimestre actual")
  public void queElAlumnoYaEstaInscritoAlCurso(String codigoCurso) {
    // Primero inscribir al alumno
    elAlumnoSeInscribeAlCurso(codigoCurso);

    // Verificar que la inscripción fue exitosa
    assertEquals(HttpStatus.CREATED, inscripcionResult.getStatus());

    InscripcionDTO inscripcion = inscripcionResult.getResponseBody().blockFirst();
    codigoInscripcionGuardado = inscripcion.getCodigoDeInscripcion();
  }

  @Y("se le informa que ya está inscrito en ese curso")
  public void seLaInformaQueYaEstaInscritoEnEseCurso() {
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Y("se le informa que las inscripciones están cerradas para este cuatrimestre")
  public void seLaInformaQueLasInscripcionesEstanCerradasParaEsteCuatrimestre() {
    assertTrue(inscripcionResult.getStatus().is4xxClientError());
  }

  @Cuando("el alumno consulta sus inscripciones")
  public void elAlumnoConsultaSusInscripciones() {

    consultaResult =
        webTestClient
            .get()
            .uri("/api/inscripciones/misInscripciones")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(List.class);
  }

  @Entonces("se le informan {int} inscripciones")
  public void seLaInformanInscripciones(int cantidadEsperada) {
    assertEquals(HttpStatus.OK, consultaResult.getStatus());

    List inscripciones = consultaResult.getResponseBody().blockFirst();
    assertNotNull(inscripciones);
    assertEquals(cantidadEsperada, inscripciones.size());
  }

  @Y("cada inscripción incluye el código de inscripción, curso, cuatrimestre, fecha y horario")
  public void cadaInscripcionIncluyeLosDatos() {
    List<Map> inscripciones = consultaResult.getResponseBody().blockFirst();
    for (Map inscripcion : inscripciones) {
      assertNotNull(inscripcion.get("codigoDeInscripcion"));
      assertNotNull(inscripcion.get("codigoCurso"));
      assertNotNull(inscripcion.get("codigoCuatrimestre"));
      assertNotNull(inscripcion.get("fecha"));
      assertNotNull(inscripcion.get("horario"));
    }
  }

  @Cuando("el alumno cancela su inscripción con código de inscripción obtenido")
  public void elAlumnoCancelaSuInscripcionConCodigoObtenido() {
    assertNotNull(codigoInscripcionGuardado, "No hay código de inscripción guardado");

    eliminarResult =
        webTestClient
            .delete()
            .uri("/api/inscripciones/" + codigoInscripcionGuardado)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Cuando("el alumno cancela su inscripción con código de inscripción {long}")
  public void elAlumnoCancelaSuInscripcionConCodigo(Long codigoInscripcion) {

    eliminarResult =
        webTestClient
            .delete()
            .uri("/api/inscripciones/" + codigoInscripcion)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Entonces("la inscripción se cancela exitosamente")
  public void laInscripcionSeCancelaExitosamente() {
    assertEquals(HttpStatus.OK, eliminarResult.getStatus());
  }

  @Entonces("no se puede cancelar la inscripción exitosamente")
  public void noSePuedeCancelarLaInscripcionExitosamente() {
    assertTrue(
        eliminarResult.getStatus().is4xxClientError()
            || eliminarResult.getStatus().is5xxServerError());
  }

  @Y("se le informa que la inscripción no existe")
  public void seLaInformaQueLaInscripcionNoExiste() {
    assertTrue(eliminarResult.getStatus().is4xxClientError());
  }
}
