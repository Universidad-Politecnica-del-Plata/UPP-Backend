package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.CarreraDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class modificar_datos_de_carrera_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<CarreraDTO> result;

  @Cuando(
      "se modifica la carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string}")
  public void seModificaLaCarreraSinPlanesDeEstudio(
      String codigo, String nombre, String titulo, String incumbencias) {

    CarreraDTO carreraEnviada = new CarreraDTO(codigo, nombre, titulo, incumbencias, null);

    this.result =
        webTestClient
            .put()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(carreraEnviada)
            .exchange()
            .returnResult(CarreraDTO.class);
  }

  @Cuando(
      "se modifica la carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string} y planes de estudio {string}")
  public void seModificaLaCarreraConUnPlanDeEstudio(
      String codigo, String nombre, String titulo, String incumbencias, String plan) {

    List<String> planesDeEstudio = List.of(plan);

    CarreraDTO carreraEnviada =
        new CarreraDTO(codigo, nombre, titulo, incumbencias, planesDeEstudio);

    this.result =
        webTestClient
            .put()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(carreraEnviada)
            .exchange()
            .returnResult(CarreraDTO.class);
  }

  @Cuando(
      "se modifica la carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string} y planes de estudio {string} y {string}")
  public void seModificaLaCarreraConDosPlanesDeEstudio(
      String codigo,
      String nombre,
      String titulo,
      String incumbencias,
      String plan1,
      String plan2) {

    List<String> planesDeEstudio = Arrays.asList(plan1, plan2);

    CarreraDTO carreraEnviada =
        new CarreraDTO(codigo, nombre, titulo, incumbencias, planesDeEstudio);

    this.result =
        webTestClient
            .put()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(carreraEnviada)
            .exchange()
            .returnResult(CarreraDTO.class);
  }

  @Cuando(
      "se modifica la carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string} y sin planes de estudio")
  public void seModificaLaCarreraRemoviendoPlanesDeEstudio(
      String codigo, String nombre, String titulo, String incumbencias) {

    CarreraDTO carreraEnviada = new CarreraDTO(codigo, nombre, titulo, incumbencias, null);

    this.result =
        webTestClient
            .put()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(carreraEnviada)
            .exchange()
            .returnResult(CarreraDTO.class);
  }

  @Entonces("se actualiza la información de la carrera {string} exitosamente")
  public void seActualizaLaInformacionDeLaCarreraExitosamente(String codigo) {
    assertEquals(HttpStatus.OK, result.getStatus());

    var resultGetCarrera =
        webTestClient
            .get()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CarreraDTO.class);

    assertEquals(HttpStatus.OK, resultGetCarrera.getStatus());
  }

  @Entonces("no se actualiza la información de la carrera exitosamente")
  public void noSeActualizaLaInformacionDeLaCarreraExitosamente() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }
}