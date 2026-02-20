package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.ActaDTO;
import com.upp.dto.ActaRequestDTO;
import com.upp.model.EstadoActa;
import com.upp.model.TipoDeActa;
import com.upp.repository.ActaRepository;
import com.upp.steps.shared.AuthHelper;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class abrir_acta_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private ActaRepository actaRepository;
  @Autowired private TokenHolder tokenHolder;
  @Autowired private AuthHelper authHelper;

  private FluxExchangeResult<ActaDTO> result;
  private ActaDTO actaCreada;

  @Cuando("el docente abre un acta de {string} para el curso {string}")
  public void elDocenteAbreUnActaDeTipoParaElCurso(String tipoActa, String codigoCurso) {
    authHelper.loginDocente();
    ActaRequestDTO actaRequest = new ActaRequestDTO();
    actaRequest.setTipoDeActa(TipoDeActa.valueOf(tipoActa.toUpperCase()));
    actaRequest.setEstado(EstadoActa.ABIERTA);
    actaRequest.setCodigoCurso(codigoCurso);

    this.result =
        webTestClient
            .post()
            .uri("/api/actas")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(actaRequest)
            .exchange()
            .returnResult(ActaDTO.class);

    if (result.getStatus() == HttpStatus.CREATED) {
      actaCreada = result.getResponseBody().blockFirst();
    }
  }

  @Entonces("el acta queda en estado {string}")
  public void elActaQuedaEnEstado(String estadoEsperado) {
    assertEquals(HttpStatus.CREATED, result.getStatus());
    assertNotNull(actaCreada);
    assertEquals(EstadoActa.valueOf(estadoEsperado.toUpperCase()), actaCreada.getEstado());
  }

  @Entonces("no se abre el acta")
  public void noSeAbreElActa() {

    assertTrue(result.getStatus().is4xxClientError());
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }
}
