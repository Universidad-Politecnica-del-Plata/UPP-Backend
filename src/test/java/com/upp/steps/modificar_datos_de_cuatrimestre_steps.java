package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.upp.dto.CuatrimestreDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class modificar_datos_de_cuatrimestre_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<CuatrimestreDTO> result;

  @Cuando("se modifica el cuatrimestre con código {string}, fecha de inicio de clases {string}, fecha de fin de clases {string}, fecha de inicio de inscripción {string}, fecha de fin de inscripción {string}, fecha de inicio de integradores {string} y fecha de fin de integradores {string}")
  public void seModificaElCuatrimestre(String codigo, String fechaInicioClases, String fechaFinClases, 
      String fechaInicioInscripcion, String fechaFinInscripcion, String fechaInicioIntegradores, String fechaFinIntegradores) {
    
    CuatrimestreDTO cuatrimestreEnviado = new CuatrimestreDTO(
        codigo,
        LocalDate.parse(fechaInicioClases),
        LocalDate.parse(fechaFinClases),
        LocalDate.parse(fechaInicioInscripcion),
        LocalDate.parse(fechaFinInscripcion),
        LocalDate.parse(fechaInicioIntegradores),
        LocalDate.parse(fechaFinIntegradores));

    this.result =
        webTestClient
            .put()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cuatrimestreEnviado)
            .exchange()
            .returnResult(CuatrimestreDTO.class);
  }

  @Entonces("se actualiza la información del cuatrimestre {string} exitosamente")
  public void seActualizaLaInformacionDelCuatrimestreExitosamente(String codigo) {
    assertEquals(HttpStatus.OK, result.getStatus());

    var resultGetCuatrimestre =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, resultGetCuatrimestre.getStatus());
  }

  @Entonces("el cuatrimestre {string} tiene fecha de inicio {string} y fecha de fin {string}")
  public void elCuatrimestreTieneFechaDeInicioYFin(String codigo, String fechaInicio, String fechaFin) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);
    assertEquals(LocalDate.parse(fechaInicio), cuatrimestre.getFechaDeInicioClases());
    assertEquals(LocalDate.parse(fechaFin), cuatrimestre.getFechaDeFinClases());
  }

  @Entonces("el cuatrimestre {string} tiene período de inscripción desde {string} hasta {string}")
  public void elCuatrimestreTienePeriodoDeInscripcion(String codigo, String fechaInicio, String fechaFin) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);
    assertEquals(LocalDate.parse(fechaInicio), cuatrimestre.getFechaInicioPeriodoDeInscripcion());
    assertEquals(LocalDate.parse(fechaFin), cuatrimestre.getFechaFinPeriodoDeInscripcion());
  }

  @Entonces("el cuatrimestre {string} tiene período de integradores desde {string} hasta {string}")
  public void elCuatrimestreTienePeriodoDeIntegradores(String codigo, String fechaInicio, String fechaFin) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);
    assertEquals(LocalDate.parse(fechaInicio), cuatrimestre.getFechaInicioPeriodoIntegradores());
    assertEquals(LocalDate.parse(fechaFin), cuatrimestre.getFechaFinPeriodoIntegradores());
  }

  @Entonces("no se actualiza la información del cuatrimestre exitosamente")
  public void noSeActualizaLaInformacionDelCuatrimestreExitosamente() {
    assertTrue(result.getStatus() == HttpStatus.NOT_FOUND || 
               result.getStatus() == HttpStatus.BAD_REQUEST,
               "Expected NOT_FOUND or BAD_REQUEST but got: " + result.getStatus());
  }
}