package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_plan_de_estudios_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<PlanDeEstudiosResponseDTO> result;

  @Cuando(
      "se registra un nuevo plan de estudios con codigo {string}, fecha de entrada en vigencia {string}, fecha de vencimiento {string}, materias en el plan {string}, {string} y {string} y total de créditos optativos {int}")
  public void darDeAltaPlanDeEstudios(
      String codigoPlan,
      String fechaVigencia,
      String fechaVencimiento,
      String codMateria1,
      String codMateria2,
      String codMateria3,
      Integer creditosOptativos) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    List<String> materias = new ArrayList<>();
    materias.add(codMateria1);
    materias.add(codMateria2);
    materias.add(codMateria3);

    PlanDeEstudiosRequestDTO planDeEstudiosEnviado =
        new PlanDeEstudiosRequestDTO(
            codigoPlan,
            creditosOptativos,
            LocalDate.parse(fechaVigencia, formatter),
            LocalDate.parse(fechaVencimiento, formatter),
            materias);

    this.result =
        webTestClient
            .post()
            .uri("/api/planDeEstudios")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(planDeEstudiosEnviado)
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);
  }

  @Entonces("se registra el plan de estudios {string} exitosamente")
  public void seRegistraElPlanDeEstudiosConCodigoExitosamente(String codigo) {
    var resultGetPlanDeEstudios =
        webTestClient
            .get()
            .uri("/api/planDeEstudios/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);

    assertEquals(HttpStatus.OK, resultGetPlanDeEstudios.getStatus());
  }
}
