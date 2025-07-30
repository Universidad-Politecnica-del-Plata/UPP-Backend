package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.CarreraDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
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
public class dar_de_alta_carrera_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<CarreraDTO> result;

  @Cuando("se registra una nueva carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string} y planes de estudio {string} y {string}")
  public void seRegistraUnaNuevaCarreraConPlanesDeEstudio(
      String codigo,
      String nombre,
      String titulo,
      String incumbencias,
      String plan1,
      String plan2) {
    
    List<String> planesDeEstudio = Arrays.asList(plan1, plan2);
    
    CarreraDTO carreraEnviada = new CarreraDTO(
        codigo,
        nombre,
        titulo,
        incumbencias,
        planesDeEstudio
    );

    this.result = webTestClient
        .post()
        .uri("/api/carreras")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(carreraEnviada)
        .exchange()
        .returnResult(CarreraDTO.class);
  }

  @Cuando("se registra una nueva carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string} y sin planes de estudio")
  public void seRegistraUnaNuevaCarreraSinPlanesDeEstudio(
      String codigo,
      String nombre,
      String titulo,
      String incumbencias) {
    
    CarreraDTO carreraEnviada = new CarreraDTO(
        codigo,
        nombre,
        titulo,
        incumbencias,
        null
    );

    this.result = webTestClient
        .post()
        .uri("/api/carreras")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(carreraEnviada)
        .exchange()
        .returnResult(CarreraDTO.class);
  }

  @Cuando("se registra una nueva carrera con codigo {string}, nombre {string}, titulo {string}, incumbencias {string} y planes de estudio {string}")
  public void seRegistraUnaNuevaCarreraConUnPlanDeEstudio(
      String codigo,
      String nombre,
      String titulo,
      String incumbencias,
      String plan) {
    
    List<String> planesDeEstudio = List.of(plan);
    
    CarreraDTO carreraEnviada = new CarreraDTO(
        codigo,
        nombre,
        titulo,
        incumbencias,
        planesDeEstudio
    );

    this.result = webTestClient
        .post()
        .uri("/api/carreras")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(carreraEnviada)
        .exchange()
        .returnResult(CarreraDTO.class);
  }

  @Entonces("se registra la carrera {string} exitosamente")
  public void seRegistraLaCarreraExitosamente(String codigo) {
    var resultGetCarrera = webTestClient
        .get()
        .uri("/api/carreras/{codigo}", codigo)
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .exchange()
        .returnResult(CarreraDTO.class);

    assertEquals(HttpStatus.OK, resultGetCarrera.getStatus());
  }

  @Entonces("no se registra la carrera exitosamente")
  public void noSeRegistraLaCarreraExitosamente() {
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }

}