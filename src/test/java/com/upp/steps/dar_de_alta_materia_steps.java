package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.MateriaDTO;
import com.upp.model.TipoMateria;
import com.upp.steps.shared.AuthHelper;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_materia_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  @Autowired private AuthHelper authHelper;

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

  @Dado("que no existe una materia con el código de materia {string}")
  public void queNoExisteUnaMateria(String codigo) {
    authHelper.loginGestorAcademico();

    // Verificamos si la materia existe y la eliminamos si es necesario
    var resultGet =
        webTestClient
            .get()
            .uri("/api/materias/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(MateriaDTO.class);

    if (resultGet.getStatus() == HttpStatus.OK) {
      // La materia existe, la eliminamos
      webTestClient
          .delete()
          .uri("/api/materias/{codigo}", codigo)
          .header("Authorization", "Bearer " + tokenHolder.getToken())
          .exchange()
          .expectStatus()
          .isOk();
    }
  }

  @Dado("que existe una materia con el código de materia {string} y nombre {string}")
  public void queExisteUnaMateria(String codigo, String nombre) {
    authHelper.loginGestorAcademico();
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
    authHelper.loginGestorAcademico();

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
}
