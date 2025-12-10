package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.ActaDTO;
import com.upp.dto.EstadoActaRequestDTO;
import com.upp.dto.NotaDTO;
import com.upp.dto.NotaRequestDTO;
import com.upp.model.Alumno;
import com.upp.model.EstadoActa;
import com.upp.repository.AlumnoRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class cargar_nota_de_alumno_en_acta_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private AlumnoRepository alumnoRepository;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<NotaDTO> resultNota;
  private NotaDTO notaCreada;
  private Long actaNumeroCorrelativo;

  @Cuando(
      "el docente carga la nota de un alumno con dni {long} y nota {int} para el curso {string}")
  public void elDocenteCargaLaNotaDeUnAlumnoConDniYNota(Long dni, Integer nota, String curso) {
    // Buscar alumno por DNI
    Alumno alumno = alumnoRepository.findByDni(dni).orElse(null);

    if (alumno != null) {
      // Buscar el acta creada (asumir que hay una sola acta abierta)
      if (actaNumeroCorrelativo == null) {
        // Obtener actas del curso para encontrar el número correlativo
        var resultActas =
            webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/actas/curso/{curso}").build(curso))
                .header("Authorization", "Bearer " + tokenHolder.getToken())
                .exchange()
                .returnResult(ActaDTO[].class);

        ActaDTO[] actas = resultActas.getResponseBody().blockFirst();
        if (actas != null && actas.length > 0) {
          actaNumeroCorrelativo = actas[0].getNumeroCorrelativo();
        }
      }

      if (actaNumeroCorrelativo != null) {
        // Crear la nota
        NotaRequestDTO notaRequest = new NotaRequestDTO();
        notaRequest.setValor(nota);
        notaRequest.setAlumnoId(alumno.getId());

        this.resultNota =
            webTestClient
                .post()
                .uri("/api/actas/{numeroCorrelativo}/notas", actaNumeroCorrelativo)
                .header("Authorization", "Bearer " + tokenHolder.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(notaRequest)
                .exchange()
                .returnResult(NotaDTO.class);

        if (resultNota.getStatus() == HttpStatus.CREATED) {
          notaCreada = resultNota.getResponseBody().blockFirst();
        }
      }
    } else {
      // Intentar con un alumnoId inexistente
      NotaRequestDTO notaRequest = new NotaRequestDTO();
      notaRequest.setValor(nota);
      notaRequest.setAlumnoId(999999L); // ID que no existe

      this.resultNota =
          webTestClient
              .post()
              .uri(
                  "/api/actas/{numeroCorrelativo}/notas",
                  actaNumeroCorrelativo != null ? actaNumeroCorrelativo : 1L)
              .header("Authorization", "Bearer " + tokenHolder.getToken())
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(notaRequest)
              .exchange()
              .returnResult(NotaDTO.class);
    }
  }

  @Cuando("el docente intenta cargar nota para un acta inexistente con numero correlativo {long}")
  public void elDocenteIntentaCargarNotaParaUnActaInexistenteConNumeroCorrelativo(
      Long numeroCorrelativo) {
    // Buscar el primer alumno disponible
    Alumno alumno = alumnoRepository.findByDni(12345678L).orElse(null);
    assertNotNull(alumno, "Debe existir un alumno para la prueba");

    NotaRequestDTO notaRequest = new NotaRequestDTO();
    notaRequest.setValor(8);
    notaRequest.setAlumnoId(alumno.getId());

    this.resultNota =
        webTestClient
            .post()
            .uri("/api/actas/{numeroCorrelativo}/notas", numeroCorrelativo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(notaRequest)
            .exchange()
            .returnResult(NotaDTO.class);
  }

  @Dado("que el acta está cerrada")
  public void queElActaEstaCerrada() {
    // Obtener el acta abierta y cerrarla
    var resultActas =
        webTestClient
            .get()
            .uri("/api/actas/curso/CURSO-001")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(ActaDTO[].class);

    ActaDTO[] actas = resultActas.getResponseBody().blockFirst();
    if (actas != null && actas.length > 0) {
      actaNumeroCorrelativo = actas[0].getNumeroCorrelativo();

      // Cerrar el acta
      EstadoActaRequestDTO estadoRequest = new EstadoActaRequestDTO();
      estadoRequest.setEstado(EstadoActa.CERRADA);

      webTestClient
          .put()
          .uri("/api/actas/{numeroCorrelativo}/estado", actaNumeroCorrelativo)
          .header("Authorization", "Bearer " + tokenHolder.getToken())
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(estadoRequest)
          .exchange()
          .expectStatus()
          .isOk();
    }
  }

  @Entonces("se guarda la información en el acta")
  public void seGuardaLaInformacionEnElActa() {
    assertEquals(HttpStatus.CREATED, resultNota.getStatus());
    assertNotNull(notaCreada);
    assertNotNull(notaCreada.getId());
    assertNotNull(notaCreada.getValor());
  }

  @Entonces("no se guarda la información en el acta")
  public void noSeGuardaLaInformacionEnElActa() {
    assertTrue(resultNota.getStatus().is4xxClientError());
    assertEquals(HttpStatus.BAD_REQUEST, resultNota.getStatus());
    assertNull(notaCreada);
  }

  @Entonces("no se puede cargar la nota por acta inexistente")
  public void noSePuedeCargarLaNotaPorActaInexistente() {
    assertEquals(HttpStatus.NOT_FOUND, resultNota.getStatus());
  }

  @Entonces("no se puede cargar la nota por acta cerrada")
  public void noSePuedeCargarLaNotaPorActaCerrada() {
    assertTrue(resultNota.getStatus().is4xxClientError());
    assertEquals(HttpStatus.BAD_REQUEST, resultNota.getStatus());
  }

  @Entonces("no se puede cargar la nota por alumno inexistente")
  public void noSePuedeCargarLaNotaPorAlumnoInexistente() {
    assertTrue(resultNota.getStatus().is4xxClientError());
    assertEquals(HttpStatus.NOT_FOUND, resultNota.getStatus());
  }

  @Entonces("no se puede cargar la nota por alumno no inscrito")
  public void noSePuedeCargarLaNotaPorAlumnoNoInscrito() {
    assertTrue(resultNota.getStatus().is4xxClientError());
    assertEquals(HttpStatus.BAD_REQUEST, resultNota.getStatus());
  }
}
