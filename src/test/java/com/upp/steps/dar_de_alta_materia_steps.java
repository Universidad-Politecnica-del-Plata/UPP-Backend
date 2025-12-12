package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.MateriaDTO;
import com.upp.model.TipoMateria;
import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_materia_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private RolRepository rolRepository;
  @Autowired private TokenHolder tokenHolder;

  private EntityExchangeResult<MateriaDTO> result;

  @Cuando(
      "se registra una materia con código de materia {string}, nombre {string}, contenidos {string}, tipo de materia {string}, cantidad de créditos que otorga {int} y créditos necesarios {int}")
  public void darDeAltaMateria(
      String codigo,
      String nombre,
      String contenidos,
      String tipoMateria,
      Integer creditosOtorga,
      Integer creditosNecesarios) {

    darDeAltaMateriaConCorrelativa(
        codigo, nombre, contenidos, tipoMateria, "", creditosOtorga, creditosNecesarios);
  }

  @Entonces("se registra la materia exitosamente")
  public void seRegistraLaMateriaExitosamente() {
    assertEquals(HttpStatus.CREATED, result.getStatus());
  }

  @Entonces("se registra la materia {string} exitosamente")
  public void seRegistraLaMateriaConCodigoExitosamente(String codigo) {
    webTestClient
        .get()
        .uri("/api/materias/{codigo}", codigo)
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(MateriaDTO.class);
  }

  @Dado("que existe una materia con el código de materia {string} y nombre {string}")
  public void queExisteUnaMateria(String codigo, String nombre) {
    darDeAltaMateria(codigo, nombre, "Contenido", "Optativa", 1, 0);
  }

  @Cuando(
      "se registra una materia con código de materia {string}, nombre {string}, contenidos {string}, tipo de materia {string}, con correlativa {string}, cantidad de créditos que otorga {int} y créditos necesarios {int}")
  public void darDeAltaMateriaConCorrelativa(
      String codigo,
      String nombre,
      String contenidos,
      String tipoMateria,
      String correlativas,
      Integer creditosOtorga,
      Integer creditosNecesarios) {

    List<String> listaDeCorrelativas =
        correlativas == null || correlativas.isBlank()
            ? List.of()
            : Arrays.stream(correlativas.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

    MateriaDTO materia =
        new MateriaDTO(
            codigo,
            nombre,
            contenidos,
            creditosOtorga,
            creditosNecesarios,
            TipoMateria.valueOf(tipoMateria.toUpperCase()),
            null,
            null,
            listaDeCorrelativas);

    this.result =
        webTestClient
            .post()
            .uri("/api/materias")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(materia)
            .exchange()
            .expectBody(MateriaDTO.class)
            .returnResult();
  }

  @Entonces("no se registra la materia exitosamente")
  public void noSeRegistraLaMateriaExitosamente() {
    assertEquals(HttpStatus.CONFLICT, result.getStatus());
  }

  @Dado("que hay un gestor academico logueado")
  public void queHayUnGestorAcademicoLogueado() {

    if (usuarioRepository.findByUsername("admin_gestion").isEmpty()) {

      Map<String, Object> registroData =
          Map.of(
              "username", "admin_gestion",
              "password", "password",
              "roles", List.of("ROLE_GESTION_ACADEMICA"));

      webTestClient
          .post()
          .uri("/api/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(registroData)
          .exchange()
          .expectStatus()
          .isCreated()
          .expectBody();
    }

    Map<String, String> loginData =
        Map.of(
            "username", "admin_gestion",
            "password", "password");

    Map<String, Object> loginResponse =
        webTestClient
            .post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginData)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();

    tokenHolder.setToken(loginResponse.get("token").toString());
  }
}
