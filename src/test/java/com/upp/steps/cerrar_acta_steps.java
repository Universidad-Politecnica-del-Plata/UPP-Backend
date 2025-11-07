package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.ActaDTO;
import com.upp.dto.ActaRequestDTO;
import com.upp.dto.EstadoActaRequestDTO;
import com.upp.model.EstadoActa;
import com.upp.model.TipoDeActa;
import com.upp.repository.ActaRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class cerrar_acta_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private ActaRepository actaRepository;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<ActaDTO> resultCrear;
  private FluxExchangeResult<ActaDTO> resultCerrar;
  private ActaDTO actaCreada;
  private ActaDTO actaCerrada;

  @Y("hay un acta de {string} abierta para el curso en el cuatrimiestre")
  public void hayUnActaDeAbiertaParaElCursoEnElCuatrimestre(String tipoActa) {
    // Crear un acta abierta para poder cerrarla después
    ActaRequestDTO actaRequest = new ActaRequestDTO();
    actaRequest.setTipoDeActa(TipoDeActa.valueOf(tipoActa.toUpperCase()));
    actaRequest.setEstado(EstadoActa.ABIERTA);
    actaRequest.setCodigoCurso("CURSO-001");

    this.resultCrear =
        webTestClient
            .post()
            .uri("/api/actas")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(actaRequest)
            .exchange()
            .returnResult(ActaDTO.class);

    if (resultCrear.getStatus() == HttpStatus.CREATED) {
      actaCreada = resultCrear.getResponseBody().blockFirst();
    }

    assertNotNull(actaCreada, "No se pudo crear el acta abierta");
    assertEquals(EstadoActa.ABIERTA, actaCreada.getEstado());
  }

  @Cuando("cierra un acta de {string} para el curso en el cuatrimestre")
  public void cierraUnActaDeParaElCursoEnElCuatrimestre(String tipoActa) {
    assertNotNull(actaCreada, "No hay acta creada para cerrar");

    // Actualizar el estado del acta a CERRADA
    EstadoActaRequestDTO estadoRequest = new EstadoActaRequestDTO();
    estadoRequest.setEstado(EstadoActa.CERRADA);

    this.resultCerrar =
        webTestClient
            .put()
            .uri("/api/actas/{numeroCorrelativo}/estado", actaCreada.getNumeroCorrelativo())
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(estadoRequest)
            .exchange()
            .returnResult(ActaDTO.class);

    if (resultCerrar.getStatus() == HttpStatus.OK) {
      actaCerrada = resultCerrar.getResponseBody().blockFirst();
    }
  }

  @Entonces("el acta queda cerrada")
  public void elActaQuedaEnEstadoCerrado() {
    assertEquals(HttpStatus.OK, resultCerrar.getStatus());
    assertNotNull(actaCerrada);
    assertEquals(EstadoActa.CERRADA, actaCerrada.getEstado());
  }
}
