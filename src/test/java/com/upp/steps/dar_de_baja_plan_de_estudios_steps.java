package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_baja_plan_de_estudios_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<PlanDeEstudiosResponseDTO> result;

  @Cuando("se da de baja el plan de estudios con codigo {string}")
  public void seDaDeBajaElPlanDeEstudiosConCodigo(String codigo) {
    this.result =
        webTestClient
            .delete()
            .uri("/api/planDeEstudios/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);
  }

  @Entonces("no existe el plan de estudios {string} en el registro")
  public void noExisteElPlanDeEstudiosEnElRegistro(String codigo) {
    var resultGetPlan =
        webTestClient
            .get()
            .uri("/api/planDeEstudios/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);
    assertEquals(HttpStatus.NOT_FOUND, resultGetPlan.getStatus());
  }

  @Entonces("no se elimina el plan de estudios y se lanza error")
  public void noSeEliminaElPlanDeEstudiosYSeLanzaError() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }
}
