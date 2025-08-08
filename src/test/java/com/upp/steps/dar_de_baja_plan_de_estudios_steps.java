package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.time.LocalDate;
import java.util.Collections;

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

  @Entonces("no se elimina el plan de estudios por tener materias asociadas")
  public void noSeEliminaElPlanDeEstudiosPorTenerMateriasAsociadas() {
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }

  @Dado("se registra un nuevo plan de estudios con codigo {string}, fecha de entrada en vigencia {string}, fecha de vencimiento {string}, sin materias y total de créditos optativos {int}")
  public void seRegistraUnNuevoPlanDeEstudiosSinMaterias(String codigo, String fechaEntrada, String fechaVencimiento, int creditosOptativos) {
    PlanDeEstudiosRequestDTO planDTO = new PlanDeEstudiosRequestDTO(
        codigo,
        creditosOptativos,
        LocalDate.of(2025, 1, 1),
        LocalDate.of(2035, 12, 31),
        Collections.emptyList()
    );
    
    webTestClient
        .post()
        .uri("/api/planDeEstudios")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .bodyValue(planDTO)
        .exchange()
        .expectStatus()
        .isCreated();
  }
}
