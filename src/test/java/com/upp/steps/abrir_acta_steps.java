package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.ActaDTO;
import com.upp.dto.ActaRequestDTO;
import com.upp.model.EstadoActa;
import com.upp.model.Rol;
import com.upp.model.TipoDeActa;
import com.upp.model.Usuario;
import com.upp.repository.ActaRepository;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class abrir_acta_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private ActaRepository actaRepository;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<ActaDTO> result;
  private ActaDTO actaCreada;

  @Dado("que hay un docente logueado")
  public void queHayUnDocenteLogueado() {
    Rol rolDocente =
        rolRepository
            .findById("ROLE_DOCENTE")
            .orElseThrow(() -> new RuntimeException("ROLE_DOCENTE no encontrado"));

    Usuario usuarioExistente = usuarioRepository.findByUsername("docente_test").orElse(null);

    if (usuarioExistente == null) {
      Map<String, Object> registroData =
          Map.of(
              "username", "docente_test",
              "password", "password",
              "roles", List.of("ROLE_DOCENTE"));

      webTestClient
          .post()
          .uri("/api/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(registroData)
          .exchange()
          .expectStatus()
          .isCreated();
    }

    Map<String, String> loginData = Map.of("username", "docente_test", "password", "password");

    String token =
        webTestClient
            .post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginData)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Map.class)
            .getResponseBody()
            .blockFirst()
            .get("token")
            .toString();

    tokenHolder.setToken(token);
  }

  @Cuando("el docente abre un acta de {string} para el curso {string}")
  public void elDocenteAbreUnActaDeTipoParaElCurso(String tipoActa, String codigoCurso) {
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
}