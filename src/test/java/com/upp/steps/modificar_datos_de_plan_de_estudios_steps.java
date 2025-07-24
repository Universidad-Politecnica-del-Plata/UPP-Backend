package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.PlanDeEstudiosRequestDTO;
import com.upp.dto.PlanDeEstudiosResponseDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class modificar_datos_de_plan_de_estudios_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<PlanDeEstudiosResponseDTO> result;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Cuando(
      "al plan de estudios con código {string} se modifica fecha de entrada en vigencia {string}, fecha de vencimiento {string}, materias en el plan {string} y {string} y total de créditos optativos {int}")
  public void modificarDatosDePlanDeEstudios(
      String codigo,
      String fechaEntrada,
      String fechaVencimiento,
      String materia1,
      String materia2,
      Integer creditosOptativos) {

    LocalDate fechaEntradaVigencia = LocalDate.parse(fechaEntrada, formatter);
    LocalDate fechaVenc = LocalDate.parse(fechaVencimiento, formatter);

    List<String> listaDeMaterias = List.of(materia1, materia2);

    PlanDeEstudiosRequestDTO planEnviado =
        new PlanDeEstudiosRequestDTO(
            codigo, creditosOptativos, fechaEntradaVigencia, fechaVenc, listaDeMaterias);

    this.result =
        webTestClient
            .put()
            .uri("/api/planDeEstudios/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(planEnviado)
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);
  }

  @Entonces("se actualiza la información del plan de estudios exitosamente")
  public void seActualizaElPlanDeEstudiosExitosamente() {
    assertEquals(HttpStatus.OK, result.getStatus());
  }

  @Entonces("no se puede modificar el plan de estudios inexistente")
  public void noSePuedeModificarPlanDeEstudiosInexistente() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }

  @Entonces("no se puede modificar el plan de estudios por materia inexistente")
  public void noSePuedeModificarPorMateriaInexistente() {
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }

  @Entonces(
      "el plan de estudios {string} tiene fecha de entrada en vigencia {string}, fecha de vencimiento {string}, materias en el plan {string} y {string} y total de créditos optativos {int}")
  public void elPlanDeEstudiosTieneLosDatosEsperados(
      String codigo,
      String fechaEntrada,
      String fechaVencimiento,
      String materia1,
      String materia2,
      int creditosOptativos) {

    var response =
        webTestClient
            .get()
            .uri("/api/planDeEstudios/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(PlanDeEstudiosResponseDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    PlanDeEstudiosResponseDTO planDeEstudios = response.getResponseBody().blockFirst();
    assertNotNull(planDeEstudios);

    LocalDate fechaEntradaEsperada = LocalDate.parse(fechaEntrada, formatter);
    LocalDate fechaVencimientoEsperada = LocalDate.parse(fechaVencimiento, formatter);

    List<String> materiasEsperadas = List.of(materia1, materia2);

    assertEquals(codigo, planDeEstudios.getCodigoDePlanDeEstudios());
    assertEquals(fechaEntradaEsperada, planDeEstudios.getFechaEntradaEnVigencia());
    assertEquals(fechaVencimientoEsperada, planDeEstudios.getFechaVencimiento());
    assertEquals(creditosOptativos, planDeEstudios.getCreditosElectivos());
    assertEquals(materiasEsperadas.size(), planDeEstudios.getCodigosMaterias().size());
    assertTrue(planDeEstudios.getCodigosMaterias().containsAll(materiasEsperadas));
  }
}
