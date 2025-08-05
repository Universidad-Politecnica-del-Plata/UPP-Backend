package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.CarreraDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_baja_carrera_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<CarreraDTO> result;

  @Cuando("se da de baja la carrera con codigo {string}")
  public void seDaDeBajaLaCarreraConCodigo(String codigo) {

    this.result =
        webTestClient
            .delete()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CarreraDTO.class);
  }

  @Entonces("se elimina el registro de la carrera {string} exitosamente")
  public void seEliminaElRegistroDeLaCarreraExitosamente(String codigo) {
    assertEquals(HttpStatus.OK, result.getStatus());

    var resultGetCarrera =
        webTestClient
            .get()
            .uri("/api/carreras/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CarreraDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, resultGetCarrera.getStatus());
  }

  @Entonces("no se elimina el registro de la carrera exitosamente")
  public void noSeEliminaElRegistroDeLaCarreraExitosamente() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }
}
